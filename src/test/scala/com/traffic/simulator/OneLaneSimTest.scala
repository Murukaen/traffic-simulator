package com.traffic.simulator

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by razvan on 26.02.2016.
  */
class OneLaneSimTest extends FlatSpec with Matchers {
  val stepCounter = 1000
  val minAxis = 0
  val maxAxis = 100
  val minSpeed = 0.5
  val MaxSpeed = 5
  val spawnRate = 0.5
  "IllegalArgumentException" should "be thrown for a negative steps parameter" in {
    intercept[IllegalArgumentException] {
      new OneLaneSim(-2, new OneLaneSimParams(minAxis, maxAxis, minSpeed, MaxSpeed, spawnRate))
    }
  }
  "At each step, the cars" should "be in descending order by position" in {
    val sim = new OneLaneSim(stepCounter, new OneLaneSimParams(minAxis, maxAxis, minSpeed, MaxSpeed, spawnRate))
    def each(s: Simulation) = {
      if (sim.cars.size > 1)
        sim.cars.sliding(2).foreach(pair => assert(pair.tail.head.currentPos < pair.head.currentPos))
    }
    sim.start(each)
  }
  "The cars" should "be in valid positions at each step" in {
    val sim = new OneLaneSim(stepCounter, new OneLaneSimParams(minAxis, maxAxis, minSpeed, MaxSpeed, spawnRate))
    def each(s: Simulation) = {
      if (sim.cars.size > 1)
        sim.cars.sliding(2).foreach(pair => assert(!pair.tail.head.collides(pair.head.currentPos)))
    }
    sim.start(each)
  }
}
