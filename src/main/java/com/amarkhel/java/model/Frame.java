package com.amarkhel.java.model;

import lombok.Value;

import java.util.List;
import java.util.function.Predicate;

import static com.amarkhel.java.Calculation.FRAME_BALLS_MAX_SUM;
import static com.amarkhel.java.Calculation.MINIMUM_BALL_VALUE;

@Value
public final class Frame {

    private final List<Ball> balls;

    public boolean isMaxEarned() {
        return isSpare() || isStrike();
    }

    public boolean isSpecial() {
        return isMaxEarned() || isBonus();
    }

    public boolean isBonus() {
        return having(Ball::isBonus);
    }

    public boolean isSpare() {
        return having(Ball::isSpare);
    }

    public boolean isStrike() {
        return having(Ball::isStrike);
    }

    public int getTotal() {
        return balls.stream().mapToInt(Ball::getPins).sum();
    }

    public boolean haveCorrectSum() {
        return getTotal() >= MINIMUM_BALL_VALUE && getTotal() <= FRAME_BALLS_MAX_SUM;
    }

    private boolean having(Predicate<Ball> predicate) {
        return balls.stream().anyMatch(predicate);
    }
}