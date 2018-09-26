package com.amarkhel

import org.scalatest.FunSuite

class ModelTest extends FunSuite {

  test("Created ball should be incorrect") {
    assert(!Ball(-1).isCorrect)
  }
  test("Created ball should be incorrect 2") {
    assert(!Ball(11).isCorrect)
  }
  test("Created ball should be correct") {
    assert(Ball(9).isCorrect)
  }
  test("Created frame should be spare") {
    val frame = Frame(List(Ball(9), Ball(1, isSpare = true)))
    assert(frame.isSpare && frame.isSpecial && !frame.isStrike && !frame.isBonus && frame.total == 10)
  }
  test("Created frame should be strike") {
    val frame = Frame(List(Ball(10, isStrike = true)))
    assert(frame.isStrike && frame.isSpecial && !frame.isSpare && !frame.isBonus  && frame.total == 10)
  }
  test("Created frame should be bonus") {
    val frame = Frame(List(Ball(10, isBonus = true), Ball(10, isBonus = true)))
    assert(frame.isBonus && frame.isSpecial && !frame.isStrike && !frame.isSpare && frame.total == 20)
  }
  test("Created frame should be not special") {
    val frame = Frame(List(Ball(2), Ball(6)))
    assert(!frame.isSpecial && frame.total == 8)
  }
}
