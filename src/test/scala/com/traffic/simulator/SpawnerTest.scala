package com.traffic.simulator

import org.scalatest.{Matchers, FlatSpec}
import scala.collection.mutable
import scala.collection.mutable.{HashSet, SortedSet}
import scala.util.Random

/**
  * Created by razvan on 24.02.2016.
  */
class SpawnerTest extends FlatSpec with Matchers {
  val halfLenght = Car.LENGTH / 2
  val start = 0
  val tries = 1000
  val emptySortedSet = scala.collection.mutable.SortedSet[Double]()
  "A spawner with no given car collection" should "return None if the given interval is thinner than a car's length" in {
    Spawner.spawn(0, Car.LENGTH, emptySortedSet) should be (None)
  }
  it should "return a valid position when possible" in {
    val end = Car.LENGTH * 3
    for (i <- 1 to tries) {
      val pos = Spawner.spawn(start, end, emptySortedSet)
      assert(pos != None)
      assert(pos.get > start + halfLenght && pos.get < end - halfLenght)
    }
  }
  "A spawner with given car collection" should "return None if no interval is larger than a car's length" in {
    val end = Car.LENGTH * 3
    val positions = mutable.SortedSet[Double]()
    positions += end / 4
    positions += end / 2
    positions += 3 * end / 4
    val pos = Spawner.spawn(start, end, positions)
    pos should be (None)
  }
  it should "return a valid position when possible" in {
    val end = Car.LENGTH * 12
    val positions = mutable.SortedSet[Double]()
    positions += halfLenght + Random.nextDouble() * (2 * Car.LENGTH)
    positions += 3 * Car.LENGTH + halfLenght + Random.nextDouble() * (2 * Car.LENGTH)
    positions += 6 * Car.LENGTH + halfLenght + Random.nextDouble() * (2 * Car.LENGTH)
    positions += 9 * Car.LENGTH + halfLenght + Random.nextDouble() * (2 * Car.LENGTH)
    for (i <- 1 to tries) {
      val pos = Spawner.spawn(start, end, positions)
      assert (pos != None)
      assert(positions.filter(Car.collide(_, pos.get)).isEmpty)
    }
  }
}
