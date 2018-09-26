package com.amarkhel

import org.scalatest.FunSuite

class ParserTest extends FunSuite {
  private val parser = Parser(NoOpValidator)
  import parser._

  test("Should throw wrong format error if input is empty"){
    val thrown = intercept[Exception] {
      parse("")
    }
    assert(thrown.getMessage === WRONG_FORMAT_ERROR)
  }
  test("Should throw wrong format error if input is just digit"){
    val thrown = intercept[Exception] {
      parse("1")
    }
    assert(thrown.getMessage === WRONG_FORMAT_ERROR)
  }
  test("Should throw wrong format error if input is not allowed symbol"){
    val thrown = intercept[Exception] {
      parse("a")
    }
    assert(thrown.getMessage === WRONG_FORMAT_ERROR)
  }
  test("Should throw wrong format error if bonus ball contains spare"){
    val thrown = intercept[Exception] {
      parse("X|X|X|X|X|X|X|X|X|X||5/")
    }
    assert(thrown.getMessage === SPARE_FRAME_BONUS_ERROR)
  }
  test("Should correctly parse spare frame"){
    val game = parse("5/||")
    assert(game.frames.head === Frame(List(Ball(5), Ball(5, isSpare = true))))
    assert(game.frames.head.isSpare)
  }
  test("Should correctly parse spare frame 2"){
    val game = parse("-/||")
    assert(game.frames.head === Frame(List(Ball(0), Ball(10, isSpare = true))))
    assert(game.frames.head.isSpare)
  }
  test("Should correctly parse frame with miss"){
    val game = parse("--||")
    assert(game.frames.head === Frame(List(Ball(0), Ball(0))))
    assert(!game.frames.head.isSpecial)
  }
  test("Should correctly parse frame with miss 2"){
    val game = parse("-1||")
    assert(game.frames.head === Frame(List(Ball(0), Ball(1))))
    assert(!game.frames.head.isSpecial)
  }
  test("Should correctly parse frame with miss 3"){
    val game = parse("1-||")
    assert(game.frames.head === Frame(List(Ball(1), Ball(0))))
    assert(!game.frames.head.isSpecial)
  }
  test("Should correctly parse frame with miss 4"){
    val game = parse("-X||")
    assert(game.frames.head === Frame(List(Ball(0), Ball(10, isStrike = true))))
    assert(game.frames.head.isStrike)
  }
  test("Should correctly parse strike frame"){
    val game = parse("X||")
    assert(game.frames.head === Frame(List(Ball(10, isStrike = true))))
    assert(game.frames.head.isStrike)
  }
  test("Should correctly parse bonus frame"){
    val game = parse("X||--")
    assert(game.frames.tail.head === Frame(List(Ball(0, isBonus = true), Ball(0, isBonus = true))))
    assert(game.frames.tail.head.isBonus)
  }
  test("Should correctly parse bonus frame 2"){
    val game = parse("X||-4")
    assert(game.frames.tail.head === Frame(List(Ball(0, isBonus = true), Ball(4, isBonus = true))))
    assert(game.frames.tail.head.isBonus)
  }
  test("Should correctly parse bonus frame 3"){
    val game = parse("X||-X")
    assert(game.frames.tail.head === Frame(List(Ball(0, isBonus = true), Ball(10, isBonus = true, isStrike = false))))
    assert(game.frames.tail.head.isBonus)
  }
  test("Should correctly parse bonus frame 4"){
    val game = parse("X||11")
    assert(game.frames.tail.head === Frame(List(Ball(1, isBonus = true), Ball(1, isBonus = true))))
    assert(game.frames.tail.head.isBonus)
  }
  test("Should correctly parse bonus frame 5"){
    val game = parse("X||4-")
    assert(game.frames.tail.head === Frame(List(Ball(4, isBonus = true), Ball(0, isBonus = true))))
    assert(game.frames.tail.head.isBonus)
  }
  test("Should correctly parse bonus frame 6"){
    val game = parse("X||X-")
    assert(game.frames.tail.head === Frame(List(Ball(10, isBonus = true, isStrike = false), Ball(0, isBonus = true))))
    assert(game.frames.tail.head.isBonus)
  }
  test("Should correctly parse bonus frame 7"){
    val game = parse("X||XX")
    assert(game.frames.tail.head === Frame(List(Ball(10, isBonus = true, isStrike = false), Ball(10, isBonus = true, isStrike = false))))
    assert(game.frames.tail.head.isBonus)
  }
  test("Should correctly parse bonus frame 8"){
    val game = parse("X||99")
    assert(game.frames.tail.head === Frame(List(Ball(9, isBonus = true), Ball(9, isBonus = true))))
    assert(game.frames.tail.head.isBonus)
  }
}