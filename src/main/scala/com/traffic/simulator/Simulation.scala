package com.traffic.simulator

import java.util.{Timer, TimerTask}

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by razvan on 25.02.2016.
  */
abstract class Simulation(tickPeriod: Int, steps: Int = 0) extends LazyLogging {
  val timer = new Timer()

  protected def tick

  def start() = {
    timer.scheduleAtFixedRate(new TimerTask() {
      def run() {
        tick
      }
    }, 0, tickPeriod)
  }
}
