package com.traffic.simulator

import scala.util.Random

abstract class StreamSim(steps: Int = 0, params: SimParams) extends Simulation(steps){
  private val carsOrdering = Ordering[Double].on[Car](car => -car.currentPos)
  private val _cars = scala.collection.mutable.SortedSet[Car]()(carsOrdering)

  def cars = _cars

  validateParams()

  private def validateParams() = {
    if (params.startAxis > params.endAxis)
      throw new IllegalArgumentException("startAxis must be lower than endAxis")
    if (params.minSpeed > params.maxSpeed)
      throw new IllegalArgumentException("minSpeed must be lower than maxSpeed")
    if (params.spawnRate < 0 || params.spawnRate > 1)
      throw new IllegalArgumentException("spawnRate must be in [0,1]")
  }

  protected def getRandSpeed(): Double = params.minSpeed + Random.nextDouble() * (params.maxSpeed - params.minSpeed)

  protected def getRandDestination(start: Double): Double = start + Random.nextDouble() * (params.endAxis - start)

  def logCars(str:String):String = str + " " + cars.toString()

  protected def spawn()

  protected def step()

  protected def clean() = {
    cars.retain(!_.reachedDestination)
  }

  override protected def tick() = {
    spawn()
    logger.info(logCars("Spawn"))
    step()
    logger.debug(logCars("Step"))
    clean()
    logger.debug(logCars("Remove"))
  }
}
