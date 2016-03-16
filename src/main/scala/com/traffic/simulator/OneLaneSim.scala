package com.traffic.simulator

import scala.util.Random

/**
  * Created by razvan on 2/4/2016.
  */
class OneLaneSim(steps: Int = 0, params: SimParams) extends StreamSim(steps, params) {

  private def updateCarPos(car: Car, nextCar: Car = null) = {
    val desiredPos = car.getFutureDesiredPosition
    car.currentPos = if (nextCar != null) math.min(nextCar.getImmediateBehindPosition, desiredPos) else desiredPos
  }

  override def spawn() = {
    if (Random.nextDouble() < params.spawnRate) {
      val spawnPos = Spawner.spawn(params.startAxis, params.endAxis, cars.map(_.currentPos))
      if (spawnPos != None) {
        val new_car = new Car(spawnPos.get, getRandDestination(spawnPos.get), getRandSpeed())
        cars += new_car
      }
    }
    logger.info(logCars("Spawn"))
  }

  override protected def step() = {
    if (cars.size > 0) {
      updateCarPos(cars.head)
      if (cars.size > 1)
        cars.iterator.sliding(2).foreach(pair => updateCarPos(pair.tail.head, pair.head))
    }
    logger.debug(logCars("Step"))
  }

  override protected def clean() = {
    cars.retain(car => !car.reachedDestination())
    logger.debug(logCars("Remove"))
  }
}
