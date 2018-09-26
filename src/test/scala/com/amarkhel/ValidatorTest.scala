package com.amarkhel

import org.scalatest.FunSuite
import FPCalculation._
import FullValidator._

class ValidatorTest extends FunSuite {
  private val validator = FullValidator

  private def times(n:Int, func : => Frame) = {
    (for {
      _ <- 1 to n
    } yield func).toArray
  }
  private def makeUnspecialFrame(first:Int = 2, second:Int = 3) = Frame(List(Ball(first), Ball(second)))
  private def makeSpareFrame(first:Int = 2) = Frame(List(Ball(first), Ball(FRAME_BALLS_MAX_SUM - first, isSpare = true)))
  private def makeStrikeFrame = Frame(List(Ball(MAXIMUM_BALL_VALUE, isStrike = true)))
  private def makeBonusFrame(first:Int = 5, second:Int = 5) = Frame(List(Ball(first, isBonus = true), Ball(second, isBonus = true)))
  private def makeBonusFrameSingle(first:Int = 5) = Frame(List(Ball(first, isBonus = true)))

  // Test Count frames valid
  test ("empty array of frames should throw error") {
    assert(validator.validate(Array.empty).get === COUNT_FRAME_ERROR)
  }
  test ("array of frames with wrong size(1) should throw error") {
    assert(validator.validate(Array(makeUnspecialFrame())).get === COUNT_FRAME_ERROR)
  }
  test (s"array of frames with wrong size(${COUNT_FRAMES - 1}) should throw error") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame())).get === COUNT_FRAME_ERROR)
  }
  test (s"array of frames with wrong size(${COUNT_FRAMES + 1}) should throw error") {
    assert(validator.validate(times(COUNT_FRAMES + 1, makeUnspecialFrame())).get === COUNT_FRAME_ERROR)
  }
  test (s"array of frames with correct size($COUNT_FRAMES) should create correct game") {
    assert(validator.validate(times(COUNT_FRAMES, makeUnspecialFrame())) === None)
  }
  //Test that bonus frame properly exist
  test ("Bonus frame should not exist because last main frame is spare") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeSpareFrame()).get === BONUS_FRAME_SHOULD_EXIST_ERROR)
  }
  test ("Bonus frame should not exist because last main frame is strike") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeStrikeFrame).get === BONUS_FRAME_SHOULD_EXIST_ERROR)
  }
  test ("Bonus frame should exist because last main frame is spare") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeSpareFrame() :+ makeBonusFrameSingle()) === None)
  }
  test ("Bonus frame should exist because last main frame is strike") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeStrikeFrame :+ makeBonusFrame())  === None)
  }
  // Test that bonus frame correct
  test ("Bonus frame should not exist because last main frame not spare or strike") {
    assert(validator.validate(times(COUNT_FRAMES, makeUnspecialFrame()) :+ makeBonusFrame()).get === BONUS_FRAME_INCORRECT)
  }
  test ("Bonus frame incorrect because last frame was spare, but bonus frame have 2 balls") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeSpareFrame() :+ makeBonusFrame()).get === BONUS_FRAME_INCORRECT)
  }
  test (s"Bonus frame incorrect because last frame was strike, but bonus frame have ${MAX_BALLS_COUNT - 1} balls") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeStrikeFrame :+ makeBonusFrameSingle()).get === BONUS_FRAME_INCORRECT)
  }
  test ("Bonus frame incorrect because it contains wrong count of pins(-1)") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeSpareFrame() :+ makeBonusFrameSingle(-1)).get === BONUS_FRAME_INCORRECT)
  }
  test ("Bonus frame incorrect because it contains wrong count of pins(11)") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeSpareFrame() :+ makeBonusFrameSingle(11)).get === BONUS_FRAME_INCORRECT)
  }
  test ("Bonus frame incorrect because it contains wrong count pins(11) in first ball") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeStrikeFrame :+ makeBonusFrame(11)).get === BONUS_FRAME_INCORRECT)
  }
  test ("Bonus frame incorrect because it contains wrong count pins(11) in second ball") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeStrikeFrame :+ makeBonusFrame(5, 11)).get === BONUS_FRAME_INCORRECT)
  }
  test ("Bonus frame should be correct") {
   assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeStrikeFrame :+ makeBonusFrame()) === None)
  }
  // Test that balls have correct count of pins
  test ("Frame incorrect because it contains wrong count pins(-1) in second ball") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeUnspecialFrame(5, -1)).get === BALL_COUNT_PINS_ERROR)
  }
  test ("Frame incorrect because it contains wrong count pins(11) in second ball") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeUnspecialFrame(5, 11)).get === BALL_COUNT_PINS_ERROR)
  }
  test ("Frame incorrect because it contains wrong count pins(11) in first ball") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeUnspecialFrame(11, 5)).get === BALL_COUNT_PINS_ERROR)
  }
  test ("Frame incorrect because it contains wrong count pins(-1) in first ball") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeUnspecialFrame(-1, 5)).get === BALL_COUNT_PINS_ERROR)
  }
  test ("All frames is correct") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeUnspecialFrame(1, 5)) === None)
  }
  // Test that frame have correct sum
  test (s"Frame incorrect because it contains wrong sum of pins, more then $FRAME_BALLS_MAX_SUM") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeUnspecialFrame(6, 5)).get === PIN_SUM_FRAME_ERROR)
  }
  test (s"Frame incorrect because it contains wrong sum of pins, more then $FRAME_BALLS_MAX_SUM again") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ makeUnspecialFrame(5, 6)).get === PIN_SUM_FRAME_ERROR)
  }
  //Test that strike frame correct
  test (s"strike Frame incorrect because it contains $MAX_BALLS_COUNT") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ Frame(List(Ball(0), Ball(10, isStrike = true))) :+ makeBonusFrame()).get === STRIKE_FRAME_INCORRECT)
  }
  test (s"strike Frame incorrect because it ball value not $MAXIMUM_BALL_VALUE, but 0") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ Frame(List(Ball(0, isStrike = true))) :+ makeBonusFrame()).get === STRIKE_FRAME_INCORRECT)
  }
  test (s"strike Frame incorrect because it ball value not $MAXIMUM_BALL_VALUE, but 9") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ Frame(List(Ball(9, isStrike = true))) :+ makeBonusFrame()).get === STRIKE_FRAME_INCORRECT)
  }
  test (s"strike Frame correct") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ Frame(List(Ball(10, isStrike = true))) :+ makeBonusFrame()) === None)
  }
  //Test that spare frame is correct
  test (s"spare Frame incorrect because it contains less then $MAX_BALLS_COUNT") {
    assert(validator.validate(times(COUNT_FRAMES - 2, makeUnspecialFrame()) :+ Frame(List(Ball(10, isSpare = true))) :+ makeUnspecialFrame()).get === SPARE_FRAME_INCORRECT)
  }
  test (s"spare Frame incorrect because it ball pins sum not $MAXIMUM_BALL_VALUE, but 0") {
    assert(validator.validate(times(COUNT_FRAMES - 2, makeUnspecialFrame()) :+ Frame(List(Ball(0), Ball(0, isSpare = true))) :+ makeUnspecialFrame()).get === SPARE_FRAME_INCORRECT)
  }
  test (s"spare Frame incorrect because it ball pins sum not $MAXIMUM_BALL_VALUE, but 9") {
    assert(validator.validate(times(COUNT_FRAMES - 2, makeUnspecialFrame()) :+ Frame(List(Ball(4), Ball(5, isSpare = true))) :+ makeUnspecialFrame()).get === SPARE_FRAME_INCORRECT)
  }
  test (s"spare Frame incorrect because it contains more then $MAX_BALLS_COUNT balls") {
    assert(validator.validate(times(COUNT_FRAMES - 2, makeUnspecialFrame()) :+ Frame(List(Ball(4), Ball(4), Ball(2, isSpare = true))) :+ makeUnspecialFrame()).get === SPARE_FRAME_INCORRECT)
  }
  test (s"spare Frame correct") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ Frame(List(Ball(4),Ball(6, isSpare = true))) :+ makeBonusFrameSingle()) === None)
  }
  // Test usual frame(not spare and not strike)
  test (s"Frame incorrect because it contains less then $MAX_BALLS_COUNT balls") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ Frame(List(Ball(6)))).get === FRAME_INCORRECT)
  }
  test (s"Frame incorrect because it contains more then $MAX_BALLS_COUNT balls") {
    assert(validator.validate(times(COUNT_FRAMES - 1, makeUnspecialFrame()) :+ Frame(List(Ball(3), Ball(3), Ball(3)))).get === FRAME_INCORRECT)
  }
}