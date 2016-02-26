package com.traffic.simulator

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by razvan on 25.02.2016.
  */
abstract class Simulation(steps: Int) extends LazyLogging {

  private var stepDone: (Simulation) => Unit = null

  if (steps < 0)
    throw new IllegalArgumentException("step number must be positive")

  val stepCount = steps

  protected def step

  private def tick(cnt: Int): Unit = {
    if (cnt > 0) {
      step
      if (stepDone != null)
        stepDone(this)
      tick(cnt - 1)
    }
  }

  def start(stepDone: (Simulation) => Unit = null): Unit = {
    this.stepDone = stepDone
    tick(stepCount)
  }
}
