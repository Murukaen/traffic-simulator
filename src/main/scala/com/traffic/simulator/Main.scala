package com.traffic.simulator

import com.typesafe.scalalogging.Logger

/**
  * Created by razvan on 2/4/2016.
  */
object Main {

  val steps = 10; //number of execution steps

  def main(args: Array[String]) {
    new OneLaneSim(steps, new OneLaneSimParams(0, 100, 1, 5, 0.6)).start()
  }
}
