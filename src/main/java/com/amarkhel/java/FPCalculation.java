package com.amarkhel.java;

import com.amarkhel.java.model.Ball;
import com.amarkhel.java.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
/**
 This calculation algorithm implemented in Functional Programming style(all is immutable and have no side-effects).
 At the beginning it create 'state' object, that contains list of non-visited balls(initialised as all balls from game)
 and current total(initialised as 0).
 Then it traverse list of all balls from the game and apply function 'calcBall' to the each element.
 Then it add calculated value to current total and drop first element from the list.
 Then it pass those intermediate total and dropped list to the next iteration.
 When all balls are visited, it extract 'total' field from the 'state' object and return it as a final result.
 All business logic related to score calculation is split to the list of separate small independent rules, each represent one business case.
 It makes logic very flexible, because each rule can be added, deleted or changed separately.
 However, comparing to imperative implementation it makes harder to fully grasp all algorithm, because it spread across the rules.
 Each rule have a form Ball => Int, so 'calcBall' function applies it one by one to the current ball and then return it sum.
 */
public final class FPCalculation implements Calculation {

    private final List<BiFunction<List<Ball>, Ball, Integer>> rules = new ArrayList<BiFunction<List<Ball>, Ball, Integer>>() {{
        add(strikeBonusRule());
        add(spareBonusRule());
        add(regularBallRule());
    }};

    @Override
    public int score(Game game) {
        /*
           Unfortunately there is no good equivalent of Scala's foldLeft, so intermediate state should be created implicitly.
           However, this state never escapes method boundary, so it can be counted as effectively-immutable implementation.
         */
        List<Ball> state = new ArrayList<>(game.getBalls());
        return game.getBalls().stream().mapToInt(ball -> calcBall(state, ball)).sum();
    }

    private int calcBall(List<Ball> state, Ball current) {
        state.remove(current);
        return rules.stream().mapToInt(r -> r.apply(state, current)).sum();
    }

    private BiFunction<List<Ball>, Ball, Integer> strikeBonusRule() {
        return (state, ball) -> ball.isStrike() ? state.get(0).getPins() + state.get(1).getPins() : 0;
    }

    private BiFunction<List<Ball>, Ball, Integer> spareBonusRule() {
        return (state, ball) -> ball.isSpare() ? state.get(0).getPins() : 0;
    }

    private BiFunction<List<Ball>, Ball, Integer> regularBallRule() {
        return (state, ball) -> ball.isBonus() ? 0 : ball.getPins();
    }
}