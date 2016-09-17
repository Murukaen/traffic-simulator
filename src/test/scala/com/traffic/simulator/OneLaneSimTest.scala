package com.traffic.simulator

import org.scalatest.FlatSpec

import scala.collection.mutable.Map

/**
  * Created by razvan on 26.02.2016.
  */
class OneLaneSimTest extends FlatSpec {
  val stepCounter = 5000
  val minAxis = 0
  val maxAxis = 1000
  val minSpeed = 0.5
  val maxSpeed = 100
  val spawnRate = 0.5
  "IllegalArgumentException" should "be thrown for a negative steps parameter" in {
    intercept[IllegalArgumentException] {
      new OneLaneSim(-2, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    }
  }
  it should "be thrown if startAxis is greater than endAxis" in {
    intercept[IllegalArgumentException] {
      new OneLaneSim(stepCounter, new SimParams(100, 10, minSpeed, maxSpeed, spawnRate))
    }
  }
  it should "be thrown if minSpeed is greater than maxSpeed" in {
    intercept[IllegalArgumentException] {
      new OneLaneSim(stepCounter, new SimParams(minAxis, maxAxis, 10, 5, spawnRate))
    }
  }
  it should "be thrown if spawnRate is less than 0" in {
    intercept[IllegalArgumentException] {
      new OneLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, -1))
    }
  }
  it should "be thrown if spawnRate is greater than 1" in {
    intercept[IllegalArgumentException] {
      new OneLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, 2))
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
  they should "not collide" in {
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
      if (positions nonEmpty) {
        val arr = sim.cars.toArray
        for ( i <- 0 until arr.length - 1)
          for ( j <- i+1 until arr.length) {
            if ((positions contains arr(i).id) && (positions contains arr(j).id))
              assert(positions(arr(i).id) > positions(arr(j).id))
          }
      }
      positions.clear()
      sim.cars.foreach(car => positions += (car.id -> car.currentPos))
    }
    sim.start(each)
  }
  it should "be removed if it reached destination" in {
    val sim = new OneLaneSim(stepCounter, new SimParams(minAxis, maxAxis, minSpeed, maxSpeed, spawnRate))
    def each(s: Simulation) = {
      assert(sim.cars.filter(car => car.currentPos >= car.endPosition).isEmpty)
    }
    sim.start(each)
  }
}
