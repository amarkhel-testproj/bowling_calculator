package com.amarkhel

import FPCalculation._

/**
  * This trait should be implemented by each validation strategy
  */
trait Validator {

  def validate(frames:Array[Frame]): Option[String]

  val BONUS_FRAME_SHOULD_EXIST_ERROR = s"If ${COUNT_FRAMES}th frame is strike or spare, then bonus frame should present"
  val COUNT_FRAME_ERROR = s"Count of non-bonus frames should be equal to $COUNT_FRAMES"
  val BONUS_FRAME_INCORRECT = s"If previous frame was Spare, then bonus frame should contain ${MAX_BALLS_COUNT - 1} ball. If previous frame was Strike, then bonus frame should contain exactly $MAX_BALLS_COUNT balls"
  val BALL_COUNT_PINS_ERROR = s"Pins of each ball shouldn't be less then $MINIMUM_BALL_VALUE and greater then $MAXIMUM_BALL_VALUE"
  val PIN_SUM_FRAME_ERROR = s"Sum of pins for each frame shouldn't be great then $FRAME_BALLS_MAX_SUM"
  val STRIKE_FRAME_INCORRECT = s"Strike frame should contain only one ball and it pins should be $MAXIMUM_BALL_VALUE"
  val SPARE_FRAME_INCORRECT = s"Spare frame should contain $MAX_BALLS_COUNT balls and sum of its pins should be equal to $FRAME_BALLS_MAX_SUM"
  val FRAME_INCORRECT = s"Non-spare and non-strike frames should contain exactly $MAX_BALLS_COUNT balls and its sum should be less then $FRAME_BALLS_MAX_SUM"
}

/**
  * This implementation always return true, i.e not validate input at all.
  */
object NoOpValidator extends Validator {
  override def validate(frames:Array[Frame]) = None
}

/**
  * This implementation maintains a list of independent business rules, and apply it one by one to the each frame.
  * It "exit early", when first error is encountered.
  * It returns optional value(error if exist or empty optional otherwise).
  */
object FullValidator extends Validator {

  override def validate(frames:Array[Frame]) = {
    rules.foldLeft(Option.empty[String]){
      (state, rule) => {
        if(state.isEmpty) rule.run(frames) else state
      }
    }
  }

  val countFramesCorrect = (frames:Array[Frame]) => frames.count(!_.isBonus) == COUNT_FRAMES

  val bonusExist = (frames:Array[Frame]) => {
    if(frames(LAST_NOTBONUS_FRAME_INDEX).isStrike || frames(LAST_NOTBONUS_FRAME_INDEX).isSpare)
      frames.length == COUNT_FRAMES + 1
    else true
  }

  val bonusFrameCorrect = (frames:Array[Frame]) =>
    if(frames.last.isBonus)
      frames(LAST_NOTBONUS_FRAME_INDEX).isSpare && bonusSpareFrameCorrect(frames.last) ||
      frames(LAST_NOTBONUS_FRAME_INDEX).isStrike && bonusStrikeFrameCorrect(frames.last)
    else true

  val ballsHaveCorrectPins = (frame:Frame) => frame.balls.forall(_.isCorrect)

  val ballsHaveCorrectSum = (frame:Frame) => {
    if(!frame.isBonus)
      frame.total >= MINIMUM_BALL_VALUE &&
      frame.total <= FRAME_BALLS_MAX_SUM
    else true
  }

  val strikeFrameCorrect = (frame:Frame) => {
    if(frame.isStrike)
      frame.balls.size == MAX_BALLS_COUNT - 1 &&
      frame.balls.head.pins == MAXIMUM_BALL_VALUE
    else true
  }

  val spareFrameCorrect = (frame:Frame) => {
    if(frame.isSpare)
      frame.balls.size == MAX_BALLS_COUNT &&
      frame.total == FRAME_BALLS_MAX_SUM &&
      frame.balls.head.pins < MAXIMUM_BALL_VALUE
    else true
  }

  val usualFrameCorrect = (frame:Frame) => {
    if(!frame.isSpecial)
      frame.balls.size == MAX_BALLS_COUNT &&
      frame.total < FRAME_BALLS_MAX_SUM
    else true
  }

  private def bonusSpareFrameCorrect(frame:Frame) = {
    frame.balls.size == MAX_BALLS_COUNT - 1 &&
    frame.balls.head.isCorrect
  }

  private def bonusStrikeFrameCorrect(frame:Frame) = {
    frame.balls.size == MAX_BALLS_COUNT &&
    frame.balls.forall(_.isCorrect)
  }

  private case class Rule(op: Array[Frame] => Boolean, message:String){
    def run(frames:Array[Frame]): Option[String] = {
      if(op(frames)) None else Some(message)
    }
  }

  private def compositeRule(op: Frame => Boolean, message:String) =  {
    Rule(frames => frames.forall(op), message)
  }

  private val rules = List(
    Rule(countFramesCorrect, COUNT_FRAME_ERROR),
    Rule(bonusExist, BONUS_FRAME_SHOULD_EXIST_ERROR),
    Rule(bonusFrameCorrect, BONUS_FRAME_INCORRECT),
    compositeRule(ballsHaveCorrectPins, BALL_COUNT_PINS_ERROR),
    compositeRule(ballsHaveCorrectSum, PIN_SUM_FRAME_ERROR),
    compositeRule(strikeFrameCorrect, STRIKE_FRAME_INCORRECT),
    compositeRule(spareFrameCorrect, SPARE_FRAME_INCORRECT),
    compositeRule(usualFrameCorrect, FRAME_INCORRECT)
  )
}