package com.traffic.simulator

/**
  * Created by razvan on 2/4/2016.
  */
object Main {

  val tickPeriod = 3000; //milliseconds

  def main(args: Array[String]) {
    new Core(tickPeriod).start()
  }
}
