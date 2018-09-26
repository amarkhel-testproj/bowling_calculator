package com.amarkhel

/**
  * Trait, that should be implemented by each algorithm for bowling score calculation.
  * It takes game object as parameter and calculate final score(represent as integer), according to implementation details.
  * It can throw Exception if 'game' object is not properly constructed, however, it is implementation-specific behavior.
  */
trait Calculation {
  val COUNT_FRAMES = 10
  val LAST_NOTBONUS_FRAME_INDEX = COUNT_FRAMES - 1
  val MINIMUM_BALL_VALUE = 0
  val MAXIMUM_BALL_VALUE = 10
  val FRAME_BALLS_MAX_SUM = MAXIMUM_BALL_VALUE
  val MAX_BALLS_COUNT = 2

  def score(game:Game): Int
}