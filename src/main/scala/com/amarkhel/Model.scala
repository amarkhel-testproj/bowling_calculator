package com.amarkhel

import FPCalculation._

case class Ball(pins:Int, isBonus:Boolean = false, isStrike:Boolean = false, isSpare:Boolean = false){
  val isCorrect = pins >= MINIMUM_BALL_VALUE && pins <= MAXIMUM_BALL_VALUE
}

case class Frame(balls:Seq[Ball]){
  val isBonus = balls.exists(_.isBonus)
  val isSpare = balls.exists(_.isSpare)
  val isStrike = balls.exists(_.isStrike)
  val total: Int = balls.map(_.pins).sum
  val isSpecial = isSpare || isStrike || isBonus
}

case class Game(frames:Seq[Frame]){
  lazy val balls = frames.flatMap(_.balls)
}

case class State(remaining:Seq[Ball], sum:Int) {
  def add(points: Int) = State(remaining.tail, sum + points)
}