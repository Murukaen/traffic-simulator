package com.traffic.simulator

import java.util.{TimerTask, Timer}

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by razvan on 2/4/2016.
  */
class Core (tickPeriod: Int) extends LazyLogging{

  val timer = new Timer()

  def tick() = logger.debug("tick")

  def start() = {
    timer.scheduleAtFixedRate(new TimerTask() {
      def run() {
        tick()
      }
    }, 0, tickPeriod)
  }
}
