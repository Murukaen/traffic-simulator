package com.traffic.simulator

/**
  * Created by razvan on 2/4/2016.
  */
object Main {

  val tickPeriod = 10; //milliseconds
  val steps = 50; //no of execution steps

  def main(args: Array[String]) {
    new OneLaneSim(tickPeriod, steps).start()
  }
}
