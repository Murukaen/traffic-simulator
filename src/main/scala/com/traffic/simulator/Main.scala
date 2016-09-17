package com.traffic.simulator

object Main {

  val steps = 10; //number of execution steps

  def main(args: Array[String]) {
    new OneLaneSim(steps, new SimParams(0, 100, 1, 5, 0.6)).start()
  }
}
