package com.amarkhel

/**
   This calculation algorithm implemented in classic imperative style(with mutability and side-effects).
   It makes it harder to reason about code, but more performant and as a bonus(questionable) it contains all logic
   related to 'score' calculation in one place, so whole algorithm can be understood easily.
   However, in case of business-logic change, it will be harder to modify this code. because it highly coupled
   and all conditions should be taken in account while refactoring.
   This implementation traverse all balls from the game and keep state in 2 variables("score" and "index").
   It analyse current ball and add to the total appropriate value, calculated according to the business rules,
   then advance 'index' variable and proceed to the next ball.
   When all balls are traversed, it return "score" as final result.
  */
object ClassicCalculation extends Calculation {

  override def score(game:Game): Int = {
    implicit val rolls = game.balls.map(_.pins).toArray
    var score = 0
    var index = 0
    for {
      _ <- 0 until COUNT_FRAMES
    } yield {
      if (isStrike(rolls(index))) {
        score += MAXIMUM_BALL_VALUE + nextTwoBallsForStrike(index)
        index += 1
      }
      else if (isSpare(index)) {
        score += MAXIMUM_BALL_VALUE + nextBallForSpare(rolls(index + 2))
        index += 2
      }
      else {
        score += twoBallsInFrame(index)
        index += 2
      }
    }
    score
  }

  private def twoBallsInFrame(index: Int)(implicit rolls:Array[Int]) = {
    rolls(index) + rolls(index + 1)
  }

  private def nextBallForSpare(roll: Int) = roll

  private def nextTwoBallsForStrike(index: Int)(implicit rolls:Array[Int]) = {
    rolls(index + 1) + rolls(index + 2)
  }

  private def isStrike(roll: Int) = roll == MAXIMUM_BALL_VALUE

  private def isSpare(index: Int)(implicit rolls:Array[Int]) = {
    rolls(index) + rolls(index + 1) == MAXIMUM_BALL_VALUE
  }
}