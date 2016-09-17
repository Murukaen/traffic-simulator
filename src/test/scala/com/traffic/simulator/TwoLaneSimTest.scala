package com.traffic.simulator

import org.scalatest.{FlatSpec, Matchers}

class TwoLaneSimTest extends FlatSpec with Matchers {
  val stepCounter = 5000
  val minAxis = 0
  val maxAxis = 1000
  val minSpeed = 0.5
  val maxSpeed = 100
  val spawnRate = 0.5
  "IllegalArgumentException" should "be thrown for a negative steps parameter" in {
    intercept[IllegalArgumentException] {
      new TwoLaneSim(-2, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    }
  }
  it should "be thrown if startAxis is greater than endAxis" in {
    intercept[IllegalArgumentException] {
      new TwoLaneSim(stepCounter, new SimParams(100, 10, minSpeed, maxSpeed, spawnRate))
    }
  }
  it should "be thrown if minSpeed is greater than maxSpeed" in {
    intercept[IllegalArgumentException] {
      new TwoLaneSim(stepCounter, new SimParams(minAxis, maxAxis, 10, 5, spawnRate))
    }
  }
  it should "be thrown if spawnRate is less than 0" in {
    intercept[IllegalArgumentException] {
      new TwoLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, -1))
    }
  }
  it should "be thrown if spawnRate is greater than 1" in {
    intercept[IllegalArgumentException] {
      new TwoLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, 2))
    }
  }
  "At each step, the cars" should "be in descending order by position" in {
    val sim = new TwoLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    def each(s: Simulation) = {
      if (sim.cars.size > 1)
        sim.cars.sliding(2).foreach(pair => assert(pair.tail.head.currentPos < pair.head.currentPos))
    }
    sim.start(each)
  }
  they should "be removed when reaching destination" in {
    val sim = new TwoLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    def each(s: Simulation) = {
      assert(!sim.cars.exists(car => car.currentPos >= car.endPosition))
    }
    sim.start(each)
  }
  they should "not collide if they are on the same lane" in {
    val sim = new TwoLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    def each(s: Simulation) = {
      val arr = sim.cars.toArray
      for (i <- 0 until arr.length - 1)
        for (j <- i + 1 until arr.length) {
          if (sim.carLane(arr(i)) == sim.carLane(arr(j)) && arr(i).collides(arr(j)))
            fail()
        }
    }
    sim.start(each)
  }
}
