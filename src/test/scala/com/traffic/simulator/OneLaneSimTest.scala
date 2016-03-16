package com.traffic.simulator

import org.scalatest.{FlatSpec, Matchers}
import scala.collection.mutable.Map;

/**
  * Created by razvan on 26.02.2016.
  */
class OneLaneSimTest extends FlatSpec with Matchers {
  val stepCounter = 1000
  val minAxis = 0
  val maxAxis = 100
  val minSpeed = 0.5
  val maxSpeed = 5
  val spawnRate = 0.5
  "IllegalArgumentException" should "be thrown for a negative steps parameter" in {
    intercept[IllegalArgumentException] {
      new OneLaneSim(-2, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    }
  }
  "At each step, the cars" should "be in descending order by position" in {
    val sim = new OneLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    def each(s: Simulation) = {
      if (sim.cars.size > 1)
        sim.cars.sliding(2).foreach(pair => assert(pair.tail.head.currentPos < pair.head.currentPos))
    }
    sim.start(each)
  }
  they should "be in valid positions" in {
    val sim = new OneLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    def each(s: Simulation) = {
      if (sim.cars.size > 1)
        sim.cars.sliding(2).foreach(pair => assert(!pair.tail.head.collides(pair.head.currentPos)))
    }
    sim.start(each)
  }
  "One car" should "not bypass another" in {
    val sim = new OneLaneSim(stepCounter, new SimParams(minAxis, 1000, minSpeed, 100, spawnRate))
    var positions:Map[Int, Double] = Map()
    def each(s: Simulation) = {
      if (!positions.isEmpty) {
        val arr = sim.cars.toArray
        for ( i <- 0 until arr.size - 1)
          for ( j <- i+1 until arr.size) {
            if ((positions contains arr(i).id) && (positions contains arr(j).id))
              assert(positions(arr(i).id) > positions(arr(j).id))
          }
      }
      positions.clear()
      sim.cars.foreach(car => positions += (car.id -> car.currentPos))
    }
    sim.start(each)
  }
}
