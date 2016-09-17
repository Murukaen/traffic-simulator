package com.traffic.simulator

class Car(start: Double, end:Double, speed: Double) extends Ordered[Car]{
  val id = Car.incCounter()
  var currentPos = start
  val endPosition = end
  def getFutureDesiredPosition = math.min(currentPos + speed, endPosition)  // if (currentPos + speed > endPosition) endPosition else currentPos + speed
  def collides(pos: Double):Boolean = Car.collide(currentPos, pos)
  def collides(car: Car):Boolean = collides(car.currentPos)
  def bypasses(pos: Double): Boolean = Car.bypass(currentPos, pos)
  def bypasses(car: Car):Boolean = bypasses(car.currentPos)
  def compare(other: Car) = currentPos.compare(other.currentPos)
  def getImmediateBehindPosition():Double = currentPos - Car.LENGTH
  def reachedDestination():Boolean = currentPos >= endPosition
  override def toString():String = "[(%d)cp:%.2f,ep:%.2f,sp:%.2f]".format(id, currentPos, endPosition, speed)
}

object Car {
  val LENGTH = 10
  private var idCounter = 0
  private def incCounter() = {idCounter += 1; idCounter}
  def collide(car1: Car, car2: Car) = car1.collides(car2)
  def collide(car:Car, pos: Double) = car.collides(pos)
  def collide(pos1: Double, pos2: Double) = math.abs(pos1 - pos2) < Car.LENGTH
  def bypass(pos1: Double, pos2: Double) =  math.abs(pos1 - pos2) < Car.LENGTH / 2
  def bypass(car1: Car, car2: Car) = car1.bypasses(car2)
}
