package com.amarkhel

/**
  * This class playing role as entry-point of application.
  * It takes validator and calculation objects as parameters, and use it for input validation and score calculation accordingly.
  * It provides the list of pre-defined implementations, composed as building-blocks, using different implementations of validator and calculation.
  * 1) FPGameService instance - use FP calculation algorithm and rule-based validator
  * 2) FPGameServiceNoValidator instance - use FP calculation algorithm and no-op validator
  * 3) ClassicGameService instance - use classic imperative calculation algorithm and rule-based validator
  * 4) ClassicGameServiceNoValidator instance - use classic imperative calculation algorithm and no-op validator
  */
object FPGameService extends GameService(FullValidator, FPCalculation)
object FPGameServiceNoValidator extends GameService(NoOpValidator, FPCalculation)
object ClassicGameService extends GameService(FullValidator, ClassicCalculation)
object ClassicGameServiceNoValidator extends GameService(NoOpValidator, ClassicCalculation)

class GameService(validator:Validator, calculation:Calculation) {

  private val parser = Parser(validator)

  def calculateScore(input:String) : Int = {
    calculation.score(parser.parse(input))
  }
}