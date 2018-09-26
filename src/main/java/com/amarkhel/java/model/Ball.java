package com.amarkhel.java.model;

import com.amarkhel.java.ParseException;
import lombok.*;
import static com.amarkhel.java.Calculation.*;

@Value
public final class Ball {

    private static final String SPARE_FRAME_BONUS_ERROR = "Bonus ball can't be spare";

    private final int pins;
    private final boolean isBonus;
    private final boolean isSpare;
    private final boolean isStrike;

    public boolean isCorrect() {
        return pins >= MINIMUM_BALL_VALUE && pins <= MAXIMUM_BALL_VALUE;
    }

    public static Ball miss(boolean isBonus) {
        return new Ball(MINIMUM_BALL_VALUE, isBonus, false, false);
    }

    public static Ball spare(int pins, boolean isBonus) {
        if (isBonus) {
            throw new ParseException(SPARE_FRAME_BONUS_ERROR);
        }
        else {
            return new Ball(pins, false, true, false);
        }
    }

    public static Ball strike(boolean isBonus) {
        return new Ball(MAXIMUM_BALL_VALUE, isBonus, false, !isBonus);
    }

    public static Ball usual(int pins, boolean isBonus) {
        return new Ball(pins, isBonus, false, false);
    }
}