package com.traffic.simulator

import scala.util.Random

/**
  * Created by razvan on 29.02.2016.
  */
abstract class StreamSim(steps: Int = 0, params: SimParams) extends Simulation(steps){
  private val carsOrdering = Ordering[Double].on[Car](car => -car.currentPos)
  private val _cars = scala.collection.mutable.SortedSet[Car]()(carsOrdering)

  def cars = _cars //TODO further isolate

  protected def getRandSpeed(): Double = params.minSpeed + Random.nextDouble() * (params.maxSpeed - params.minSpeed)

  protected def getRandDestination(start: Double): Double = start + Random.nextDouble() * (params.endAxis - start)

  def logCars(str:String):String = str + " " + cars.toString()

  protected def spawn()

  protected def step()

  protected def clean()

  override protected def tick() = {
    spawn()
    step()
    clean()
  }
}
