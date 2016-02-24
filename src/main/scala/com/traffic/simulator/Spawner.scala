package com.traffic.simulator

import scala.collection.mutable.SortedSet
import scala.util.Random

/**
  * Created by razvan on 23.02.2016.
  */
object Spawner {

  private def getRandomPos(from: Double, to:Double) = Random.nextDouble() * (to - from) + from

  private def getLenRange(p: (Double, Double)): Double = p._2 - p._1

  def pickPos(chosen:Double, intervals: List[(Double, Double)]): Double = {
    val int = intervals.head
    val len = getLenRange(int)
    if (chosen <= len)
      int._1 + chosen
    else
      pickPos(chosen - len, intervals.tail)
  }

  def spawn(minPos: Double, maxPos: Double, presentPositions: SortedSet[Double]): Option[Double] = {
    val halfLenght = Car.LENGTH / 2
    val intervals = (presentPositions + (minPos - Car.LENGTH / 2) + (maxPos + Car.LENGTH / 2)).sliding(2).toList.filter(p => !Car.collide(p.head, p.tail.head)).map(p => (p.head + Car.LENGTH, p.tail.head - Car.LENGTH))
    var availableLen:Double = 0
    intervals.foreach(p => availableLen += getLenRange(p))
    if (availableLen > 0) {
      var chosen = Random.nextDouble() * availableLen
      Some(pickPos(chosen, intervals))
    }
    else
      None
  }
}
