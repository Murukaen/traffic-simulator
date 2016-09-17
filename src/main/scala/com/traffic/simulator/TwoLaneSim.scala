package com.traffic.simulator

import scala.util.Random

class TwoLaneSim(steps: Int = 0, params: SimParams) extends StreamSim(steps, params) {

  import TwoLaneSim._

  private val _carLane = collection.mutable.Map[Car, Int]()

  def carLane = _carLane // TODO cleanup on remove

  private def updateCarPos(car: Car, nextCars: Array[Car] = Array(), bypassingCar: Car = null) = {
    cars.remove(car)
    val desiredPos = car.getFutureDesiredPosition
    val nextCarOnFirstLane = nextCars.filter(car => carLane(car) == FIRST_LANE).headOption
    val nextCarOnSecondLane = nextCars.filter(car => carLane(car) == SECOND_LANE).headOption
    if (carLane(car) == FIRST_LANE) {
      if (nextCarOnFirstLane.isEmpty || desiredPos <= nextCarOnFirstLane.get.getImmediateBehindPosition())
        car.currentPos = desiredPos
      else {
        val firstLanePostionOption = nextCarOnFirstLane.get.getImmediateBehindPosition()
        if (bypassingCar == null && (nextCarOnSecondLane.isEmpty || desiredPos <= nextCarOnSecondLane.get.getImmediateBehindPosition())) {
          car.currentPos = desiredPos
          carLane(car) = SECOND_LANE
        }
        else if (bypassingCar == null && nextCarOnSecondLane.get.getImmediateBehindPosition() > firstLanePostionOption) {
          car.currentPos = nextCarOnSecondLane.get.getImmediateBehindPosition()
          carLane(car) = SECOND_LANE
        }
        else
          car.currentPos = firstLanePostionOption
      }
    }
    else {
      val secondLanePositionOption = if (nextCarOnSecondLane.isEmpty || desiredPos < nextCarOnSecondLane.get.getImmediateBehindPosition()) desiredPos
      else nextCarOnSecondLane.get.getImmediateBehindPosition()
      if (bypassingCar == null && (nextCarOnFirstLane.isEmpty || desiredPos <= nextCarOnFirstLane.get.getImmediateBehindPosition())) {
        car.currentPos = desiredPos
        carLane(car) = FIRST_LANE
      }
      else if(bypassingCar == null && nextCarOnFirstLane.get.getImmediateBehindPosition() > secondLanePositionOption ) {
        car.currentPos = nextCarOnFirstLane.get.getImmediateBehindPosition()
        carLane(car) = FIRST_LANE
      }
      else
        car.currentPos = secondLanePositionOption
    }
    cars.add(car)
  }

  override protected def spawn(): Unit = {
    if (Random.nextDouble() < params.spawnRate) {
      val spawnPos = Spawner.spawn(params.startAxis, params.endAxis, cars.filter(car => carLane(car) == FIRST_LANE).map(_.currentPos))
      if (spawnPos.isDefined) {
        val new_car = new Car(spawnPos.get, getRandDestination(spawnPos.get), getRandSpeed())
        cars.add(new_car)
        carLane(new_car) = FIRST_LANE
      }
    }
  }

  override protected def step() = {
    if (cars.nonEmpty) {
      updateCarPos(cars.head)
      if (cars.size > 1) {
        val arr = cars.toArray
        for (i <- 1 until arr.length) {
          val bypassingCar = if (i < arr.length - 1 && arr(i).bypasses(arr(i+1))) arr(i+1) else null
          updateCarPos(arr(i), arr.slice(0, i).reverse, bypassingCar)
        }
      }
    }
  }
}

object TwoLaneSim {
  private def FIRST_LANE = 1
  private def SECOND_LANE = 2
}
