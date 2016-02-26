package com.traffic.simulator

import scala.util.Random

/**
  * Created by razvan on 2/4/2016.
  */
class OneLaneSim(steps: Int = 0, params: OneLaneSimParams) extends Simulation(steps) {
  private val carsOrdering = Ordering[Double].on[Car](car => -car.currentPos)
  private val _cars = scala.collection.mutable.SortedSet[Car]()(carsOrdering)

  def cars = _cars

  private def getRandSpeed(): Double = params.minSpeed + Random.nextDouble() * (params.maxSpeed - params.minSpeed)

  private def getRandDestination(start: Double): Double = start + Random.nextDouble() * (params.endAxis - start)

  private def updateCarPos(car: Car, nextCar: Car = null) = {
    val desiredPos = car.getFutureDesiredPosition
    car.currentPos = if (nextCar != null) math.min(nextCar.getImmediateBehindPosition, desiredPos) else desiredPos
  }

  def logCars(str:String):String = str + " " + _cars.toString()

  override protected def step() = {
    /* Spawn */
    if (Random.nextDouble() < params.spawnRate) {
      val spawnPos = Spawner.spawn(params.startAxis, params.endAxis, _cars.map(_.currentPos))
      if (spawnPos != None) {
        val new_car = new Car(spawnPos.get, getRandDestination(spawnPos.get), getRandSpeed())
        _cars += new_car
      }
    }
    logger.info(logCars("Spawn"))
    /* Step */
    if (_cars.size > 0) {
      updateCarPos(_cars.head)
      if (_cars.size > 1)
        _cars.iterator.sliding(2).foreach(pair => updateCarPos(pair.tail.head, pair.head))
    }
    logger.debug(logCars("Step"))
    /* Remove */
    _cars.retain(car => !car.reachedDestination())
    logger.debug(logCars("Remove"))
  }
}
