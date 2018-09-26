package com.amarkhel.java;

import com.amarkhel.java.model.Game;

/**
   Interface, that should be implemented by each algorithm for bowling score calculation.
   It takes game object as parameter and calculate final score(represent as integer), according to implementation details.
   It can throw Exception if 'game' object is not properly constructed, however, it is implementation-specific behavior.
 */
public interface Calculation {
    int score(Game game);

    int MAXIMUM_BALL_VALUE = 10;
    int COUNT_FRAMES = 10;
    int LAST_NOTBONUS_FRAME_INDEX = COUNT_FRAMES - 1;
    int MINIMUM_BALL_VALUE = 0;
    int FRAME_BALLS_MAX_SUM = MAXIMUM_BALL_VALUE;
    int MAX_BALLS_COUNT = 2;
}