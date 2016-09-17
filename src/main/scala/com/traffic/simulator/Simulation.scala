package com.traffic.simulator

import com.typesafe.scalalogging.{StrictLogging, LazyLogging}

abstract class Simulation(steps: Int) extends StrictLogging {

  private var stepDone: (Simulation) => Unit = null

  if (steps < 0)
    throw new IllegalArgumentException("step number must be positive")

  val stepCount = steps

  protected def tick

  private def pTick(cnt: Int): Unit = {
    if (cnt > 0) {
      tick
      if (stepDone != null)
        stepDone(this)
      pTick(cnt - 1)
    }
  }

  def start(stepDone: (Simulation) => Unit = null): Unit = {
    this.stepDone = stepDone
    pTick(stepCount)
  }
}
