package com.traffic.simulator

import java.util
import java.util.{TimerTask, Timer}

import com.typesafe.scalalogging.LazyLogging

import scala.util.Random

/**
  * Created by razvan on 2/4/2016.
  */
class OneLaneSim(tickPeriod: Int, steps: Int = 0) extends Simulation(tickPeriod, steps){

  val carsOrdering = Ordering[Double].on[Car](car => -car.currentPos)
  val cars = scala.collection.mutable.SortedSet[Car]()(carsOrdering)

  var stepCounter = 0

  private def getRandSpeed(): Double = OneLaneSim.MIN_CAR_SPEED + Random.nextDouble() * (OneLaneSim.MAX_CAR_SPEED - OneLaneSim.MIN_CAR_SPEED)

  private def getRandDestination(start:Double): Double = start + Random.nextDouble() * (OneLaneSim.END_AXIS - start)

  private def updateCarPos(car: Car, nextCar: Car = null) = {
    val desiredPos = car.getFutureDesiredPosition
    car.currentPos = if (nextCar != null) math.min(nextCar.getImmediateBehindPosition, desiredPos) else desiredPos
  }

  override protected def tick() = {
    /* Spawn */
    if (Random.nextDouble() < OneLaneSim.SPAWN_RATE) {
      val spawnPos = Spawner.spawn(OneLaneSim.START_AXIS, OneLaneSim.END_AXIS, cars.map(_.currentPos))
      if (spawnPos != None) {
        val new_car = new Car(spawnPos.get, getRandDestination(spawnPos.get), getRandSpeed())
        cars += new_car
      }
    }
    logger.info(cars.toString())
    /* Step */
    if (cars.size > 0) {
      updateCarPos(cars.head)
      if (cars.size > 1)
        cars.iterator.sliding(2).foreach(pair => updateCarPos(pair.tail.head, pair.head))
    }
    /* Remove */
    cars.retain(car => !car.reachedDestination())
    /* Count steps */
    stepCounter += 1
    if (stepCounter == steps)
      timer.cancel()
  }
}

object OneLaneSim {
  val START_AXIS:Double = 0
  val END_AXIS:Double = 100
  val MIN_CAR_SPEED: Double = 1
  val MAX_CAR_SPEED:Double = 5
  val SPAWN_RATE:Double = 0.6
}
