package com.amarkhel

import com.amarkhel.java.GameService
import org.scalatest.FunSuite

class BowlingScoreTest extends FunSuite {

  type Service = {def calculateScore(value: String): Int}
  private val javaServices:List[Service] = List(GameService.fp, GameService.fpNoValidator, GameService.classic, GameService.classicNoValidator)
  private val scalaServices:List[Service] = List(FPGameService, ClassicGameService, ClassicGameServiceNoValidator, FPGameServiceNoValidator)
  private val services = javaServices ++ scalaServices
  private val javaServicesWithValidation:List[Service] = List(GameService.fp, GameService.classic)
  private val scalaServicesWithValidation:List[Service] = List(FPGameService, ClassicGameService)
  private val servicesWithValidation = javaServicesWithValidation ++ scalaServicesWithValidation

  private def run(input:String, result:Int) = {
    for {
      service <- services
    } yield assert(service.calculateScore(input) == result, s"Issue encountered in service class $service")
  }

  private def runWithValidation(input:String) = {
    for {
      service <- servicesWithValidation
    } yield assertThrows[Exception](service.calculateScore(input), s"Issue encountered in service class $service")
  }
  
  test("Game with all strikes should have score 300") {
    run("X|X|X|X|X|X|X|X|X|X||XX", 300)
  }
  test("Game with all misses should have score 0") {
    run("--|--|--|--|--|--|--|--|--|--||", 0)
  }
  test("Game with all ones should have score 20") {
    run("11|11|11|11|11|11|11|11|11|11||", 20)
  }
  test("Game with all strikes and misses in bonus frame should have score 270") {
    run("X|X|X|X|X|X|X|X|X|X||--", 270)
  }
  test("Game with all strikes and ones in bonus frame should have score 273") {
    run("X|X|X|X|X|X|X|X|X|X||11", 273)
  }
  test("Game with all 9 in first and miss in second should have score 90") {
    run("9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||", 90)
  }
  test("Game with all fives should have score 150") {
    run("5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5", 150)
  }
  test("Game with all fives and miss in bonus frame should have score 145") {
    run("5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||-", 145)
  }
  test("Input '5/|X|5/|X|5/|X|5/|X|5/|5/||X' should have score 195") {
    run("5/|X|5/|X|5/|X|5/|X|5/|5/||X", 195)
  }
  test("Input '52|42|32|42|52|62|5/|5/|X|X||32' should have score 112") {
    run("52|42|32|42|52|62|5/|5/|X|X||32", 112)
  }
  test("Game with all fives and strike in bonus frame should have score 155") {
    run("5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||X", 155)
  }
  test("Game with input 'X|7/|9-|X|-8|8/|-6|X|X|X||81' should have score 167") {
    run("X|7/|9-|X|-8|8/|-6|X|X|X||81", 167)
  }
  test("Game with input '12|34|54|72|9/|X|-1|23|45|63||' should have score 83") {
    run("12|34|54|72|9/|X|-1|23|45|63||", 83)
  }
  test("Game with input 'X|--|X|--|X|--|X|--|X|--||' should have score 50") {
    run("X|--|X|--|X|--|X|--|X|--||", 50)
  }
  test("Game with input '3-|--|--|--|--|--|--|--|--|--||' should have score 3") {
    run("3-|--|--|--|--|--|--|--|--|--||", 3)
  }
  test("Game with input 'X|--|--|--|--|--|--|--|--|--||' should have score 10") {
    run("X|--|--|--|--|--|--|--|--|--||", 10)
  }
  test("Game with input '5/|--|--|--|--|--|--|--|--|--||' should have score 10") {
    run("5/|--|--|--|--|--|--|--|--|--||", 10)
  }
  test("Game with input 'X|34|--|--|--|--|--|--|--|--||' should have score 24") {
    run("X|34|--|--|--|--|--|--|--|--||", 24)
  }
  test("Game with input '5/|34|--|--|--|--|--|--|--|--||' should have score 20") {
    run("5/|34|--|--|--|--|--|--|--|--||", 20)
  }
  test("Game with input '1/|1/|1/|1/|1/|1/|1/|1/|1/|1/||2' should have score 111") {
    run("1/|1/|1/|1/|1/|1/|1/|1/|1/|1/||2", 111)
  }
  test("Game with empty input should throw error") {
    runWithValidation("")
  }
  test("Game with one-symbol input should throw error") {
    runWithValidation("1")
  }
  test("Game with input, contains letter except 'X' should throw error") {
    runWithValidation("a")
  }
  test("Game with wrong count of frames should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|")
  }
  test("Game with wrong count of frames should throw error 2") {
    runWithValidation("X|X|X|X|X|X|X|X|X||")
  }
  test("Game with wrong count of frames should throw error 3") {
    runWithValidation("X|X|X|X|X|X|X|X|X||XX")
  }
  test("Game with last strike and no bonuses should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X")
  }
  test("Game with last strike and no bonuses should throw error 2") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X||")
  }
  test("Game with bonus contains 3 balls should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X||123")
  }
  test("Game with last strike frame and only one bonus ball should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X||1")
  }
  test("Game with spare ball in bonus should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X||5/")
  }
  test("Game with not-allowed symbol in bonus should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X||5a")
  }
  test("Game with input contains '0' should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X||05")
  }
  test("Game with last spare frame and 2 bonus balls should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|5/||22")
  }
  test("Game with last spare frame and bonus contains not-allowed symbol should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|5/||a")
  }
  test("Game with last spare frame and bonus contains not-allowed symbol should throw error 2") {
    runWithValidation("X|X|X|X|X|X|X|X|X|5/||0")
  }
  test("Game with 2 spare balls in one frame should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|//||9")
  }
  test("Game with frame that have first ball spare should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|/-||9")
  }
  test("Game with frame that have first ball spare should throw error 2") {
    runWithValidation("X|X|X|X|X|X|X|X|X|/2||9")
  }
  test("Game with non-spare and non-strike last frame, but have bonus balls should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|52||9")
  }
  test("Game with last strike frame and only one bonus ball should throw error 3") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X|X||9")
  }
  test("Game with strike frame and 2 balls in it should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X|X5||9")
  }
  test("Game with strike frame and 2 balls in it should throw error 2") {
    runWithValidation("X|X|X|X|X|X|X|X|X|X|5X||9")
  }
  test("Game with frame contains more then 2 balls should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|52/||9")
  }
  test("Game with wrong input 'X|X|X|X|X|X|X|X|X|a||9' should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|a||9")
  }
  test("Game with wrong input 'X|X|X|X|X|X|X|X|X|08||9' should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|08||9")
  }
  test("Game with wrong input 'X|X|X|X|X|X|X|X|X|80||9' should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|80||9")
  }
  test("Game with frame that have sum of balls great then 10 should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|56||9")
  }
  test("Game with frame that have non-spare and non-strike frame with only one ball should throw error") {
    runWithValidation("X|X|X|X|X|X|X|X|X|5||9")
  }
  test("Game with frame that have non-spare and non-strike frame with only one ball should throw error 2") {
    runWithValidation("X|X|X|X|X|X|X|X|X|5||")
  }
  test("Game with frame that have non-spare and non-strike frame with only one ball should throw error 3") {
    runWithValidation("X|X|X|X|X|X|X|X|X|0||")
  }
}