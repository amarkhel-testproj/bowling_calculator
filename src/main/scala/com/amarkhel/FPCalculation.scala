package com.amarkhel

/**
   This calculation algorithm implemented in Functional Programming style(all is immutable and have no side-effects).
   At the beginning it create 'state' object, that contains list of non-visited balls(initialised as all balls from game)
   and current total(initialised as 0).
   Then it traverse list of all balls from the game and apply function 'calcBall' to the each element.
   Then it add calculated value to current total and drop first element from the list.
   Then it pass those intermediate total and dropped list to the next iteration.
   When all balls are visited, it extract 'total' field from the 'state' object and return it as a final result.
   'State' object recreated at each iteration, so it introduce some performance cost comparing to classic algorithm
   with in-place mutated data.
   'List' in Scala is persistent collection, so program will not recreate whole list when we dropping element,
   just create new reference, so it have no performance cost.
   All business logic related to score calculation is split to the list of separate small independent rules, each represent one business case.
   It makes logic very flexible, because each rule can be added, deleted or changed separately.
   However, comparing to imperative implementation it makes harder to fully grasp all algorithm, because it spread across the rules.
   Each rule have a form Ball => Int, so 'calcBall' function applies it one by one to the current ball and then return it sum.
 */
object FPCalculation extends Calculation {

  override def score(game:Game): Int = {
    game.balls.foldLeft(State(game.balls, 0))(calcBall).sum
  }

  private def calcBall(state:State, current:Ball) = state.add(executeRules(state, current))

  private def executeRules(state:State, current:Ball): Int = {
    rules.foldLeft(0){
      (total, rule) => total + rule(state.remaining.tail, current)
    }
  }

  private val strikeRule = (state:Seq[Ball], ball:Ball) => if(ball.isStrike) state.head.pins + state(1).pins else 0

  private val spareRule = (state:Seq[Ball], ball:Ball) => if(ball.isSpare) state.head.pins else 0

  private val regularRule = (_:Seq[Ball], ball:Ball) => if(ball.isBonus) 0 else ball.pins

  private val rules = List(strikeRule, spareRule, regularRule)
}