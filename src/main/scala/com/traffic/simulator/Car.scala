package com.traffic.simulator

/**
  * Created by razvan on 23.02.2016.
  */
class Car(start: Double, end:Double, speed: Double) extends Ordered[Car]{
  val id = Car.incCounter()
  var currentPos = start
  def getFutureDesiredPosition = if (currentPos + speed > end) end else currentPos + speed
  def collides(pos: Double):Boolean = Car.collide(currentPos, pos)
  def collides(car: Car):Boolean = collides(car.currentPos)
  def compare(other: Car) = currentPos.compare(other.currentPos)
  def getImmediateBehindPosition():Double = currentPos - Car.LENGTH
  def reachedDestination():Boolean = currentPos >= end
  override def toString():String = "[(%d)cp:%.2f,ep:%.2f,sp:%.2f]".format(id, currentPos, end, speed)
}

object Car {
  val LENGTH = 10
  private var idCounter = 0
  private def incCounter() = {idCounter += 1; idCounter}
  def collide(car1: Car, car2: Car) = car1.collides(car2)
  def collide(car:Car, pos: Double) = car.collides(pos)
  def collide(pos1: Double, pos2: Double) = math.abs(pos1 - pos2) <= Car.LENGTH
}
