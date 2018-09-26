package com.amarkhel.java;

import com.amarkhel.java.model.Game;

import java.util.List;

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
public final class ClassicCalculation implements Calculation {

    @Override
    public int score(Game game) {
        List<Integer> rolls = game.getRolls();
        int score = 0;
        int index = 0;
        for (int i = 0; i < COUNT_FRAMES; i++) {
            if (isStrike(rolls.get(index))) {
                score += MAXIMUM_BALL_VALUE + nextTwoBallsForStrike(index, rolls);
                index += 1;
            }
            else if (isSpare(index, rolls)) {
                score += MAXIMUM_BALL_VALUE + nextBallForSpare(rolls.get(index + 2));
                index += 2;
            }
            else {
                score += twoBallsInFrame(index, rolls);
                index += 2;
            }
        }
        return score;
    }

    private int twoBallsInFrame(int index, List<Integer> rolls) {
        return rolls.get(index) + rolls.get(index + 1);
    }

    private int nextBallForSpare(int roll) {
        return roll;
    }

    private int nextTwoBallsForStrike(int index, List<Integer> rolls) {
        return rolls.get(index + 1) + rolls.get(index + 2);
    }

    private boolean isStrike(int roll) {
        return roll == MAXIMUM_BALL_VALUE;
    }

    private boolean isSpare(int index, List<Integer> rolls) {
        return rolls.get(index) + rolls.get(index + 1) == MAXIMUM_BALL_VALUE;
    }
}