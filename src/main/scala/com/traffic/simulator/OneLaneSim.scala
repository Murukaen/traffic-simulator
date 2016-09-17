package com.traffic.simulator

import scala.util.Random

class OneLaneSim(steps: Int = 0, params: SimParams) extends StreamSim(steps, params) {

  private def updateCarPos(car: Car, nextCar: Car = null) = {
    cars.remove(car)
    val desiredPos = car.getFutureDesiredPosition
    car.currentPos = if (nextCar != null) math.min(nextCar.getImmediateBehindPosition, desiredPos) else desiredPos
    cars.add(car)
  }


  override def spawn() = {
    if (Random.nextDouble() < params.spawnRate) {
      val spawnPos = Spawner.spawn(params.startAxis, params.endAxis, cars.map(_.currentPos))
      if (spawnPos.isDefined) {
        val new_car = new Car(spawnPos.get, getRandDestination(spawnPos.get), getRandSpeed())
        cars.add(new_car)
      }
    }
  }

  override def step() = {
    if (cars nonEmpty) {
      updateCarPos(cars.head)
      if (cars.size > 1)
        cars.iterator.sliding(2).foreach(pair => updateCarPos(pair.tail.head, pair.head))
    }
  }
}
