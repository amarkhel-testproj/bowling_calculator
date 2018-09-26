package com.amarkhel

import FPCalculation._

/**
  This class encapsulate all logic related to parsing and validation of input data.
  It takes 'validator' object as parameter and forward responsibility about validation to it.
  It guaranteed that Exception will be thrown when input is empty, have wrong format or didn't contain BONUS_DELIMITER(||),
  because it is very important for further algorithm. Other validation rules are enforced by 'validator' instance
  and implementation specific(it can skip validation at all or always throw error).

  Parsing algorithm:
  1) Split input by BONUS_DELIMITER, left part will represent main bowling frames and right part will represent bonus frame
  2) Parse main frames input to List of Frame objects.
  3) Try to parse bonus input(if exist one frame will be returned, wrapped as Optional or empty Optional otherwise).
  4) Combine list of balls and optional bonus frame to single list
  5) Validate constructed list, using 'validator' object
  6) If validation is successful, then new Game object will be constructed and returned as result.
     Otherwise Exception with error description will be thrown.
 */
case class Parser(validator:Validator) {

  val WRONG_FORMAT_ERROR =
    """
      Input format is wrong.
      It should have 10 frames separated by '|' and bonus frame(optionally, at the end of the input, separated by 2 preceeding '|'.
      Each frame should contains up to 2 symbols, representing balls. Allowed symbols are: digits, 'X', '/', '-'.
      Correct example is 'X|5/|22|--|33|X|X|--|4/|X||23'
    """
  val SPARE_FRAME_BONUS_ERROR = "Bonus ball can't be spare"
  val MISS  = '-'
  val STRIKE  = 'X'
  val SPARE = '/'
  val BONUS_DELIMITER = "||"
  val FRAME_DELIMITER_PATTERN = "\\|"
  val BONUS_DELIMITER_PATTERN = FRAME_DELIMITER_PATTERN + FRAME_DELIMITER_PATTERN

  def parse(input:String) : Game = {
    val (mainInput, bonusInput) = splitParts(input)
    val frames = combine(parseFrames(mainInput), parseBonus(bonusInput))
    val error = validator.validate(frames)
    if (error.isEmpty) Game(frames) else throw new Exception(error.get)
  }

  private def combine(frames:Array[Frame], bonus:Option[Frame]) = if(bonus.isDefined) frames :+ bonus.get else frames

  private def parseFrames(input:Array[String]) = input.map(f => parseFrame(f))

  private def parseBonus(input:String) = if(input.isEmpty) None else Some(parseFrame(input, isBonus = true))

  private def splitParts(input:String) = {
    if(input == null || input.isEmpty || !input.contains(BONUS_DELIMITER)) throw new Exception(WRONG_FORMAT_ERROR)
    else {
      val splitted = input.split(BONUS_DELIMITER_PATTERN)
      (splitted.head.split(FRAME_DELIMITER_PATTERN), if(splitted.size > 1) splitted.tail.head else "")
    }
  }

  private def parseFrame(input:String, isBonus:Boolean = false) = {
    Frame(input.foldLeft(List.empty[Ball])((accum, ball) => accum :+ parseBall(ball, accum, isBonus)))
  }

  private def parseBall(char:Char, balls:List[Ball], isBonus:Boolean) = char match {
    case MISS => Ball(MINIMUM_BALL_VALUE, isBonus)
    case STRIKE => Ball(MAXIMUM_BALL_VALUE, isBonus, isStrike = !isBonus)
    case SPARE => if(isBonus) throw new Exception(SPARE_FRAME_BONUS_ERROR) else Ball(remainingPins(balls), isSpare = true)
    case '1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9' => Ball(char.toString.toInt, isBonus)
    case _ => throw new Exception(WRONG_FORMAT_ERROR)
  }

  private def remainingPins(balls:List[Ball]) = FRAME_BALLS_MAX_SUM - balls.map(_.pins).sum
}