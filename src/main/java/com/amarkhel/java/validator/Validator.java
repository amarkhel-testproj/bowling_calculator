package com.amarkhel.java.validator;

import com.amarkhel.java.model.Frame;

import java.util.List;
import java.util.Optional;
import static com.amarkhel.java.Calculation.*;
/**
 * This interface should be implemented by each validation strategy
 */
public interface Validator {
    Optional<String> validate(List<Frame> frames);

    String BONUS_FRAME_SHOULD_EXIST_ERROR = String.format("If %dth frame is strike or spare, then bonus frame should present", COUNT_FRAMES);
    String COUNT_FRAME_ERROR = String.format("Count of non-bonus frames should be equal to %d", COUNT_FRAMES);
    String BONUS_FRAME_INCORRECT = String.format("If previous frame was Spare, then bonus frame should contain %d ball. If previous frame was Strike, then bonus frame should contain exactly %d balls", MAX_BALLS_COUNT - 1, MAX_BALLS_COUNT);
    String BALL_COUNT_PINS_ERROR = String.format("Pins of each ball shouldn't be less then %d and greater then %d", MINIMUM_BALL_VALUE, MAXIMUM_BALL_VALUE);
    String PIN_SUM_FRAME_ERROR = String.format("Sum of pins for each frame shouldn't be great then %d", FRAME_BALLS_MAX_SUM);
    String STRIKE_FRAME_INCORRECT = String.format("Strike frame should contain only one ball and it pins should be %d", MAXIMUM_BALL_VALUE);
    String SPARE_FRAME_INCORRECT = String.format("Spare frame should contain %d balls and sum of its pins should be equal to %d", MAX_BALLS_COUNT, FRAME_BALLS_MAX_SUM);
    String FRAME_INCORRECT = String.format("Non-spare and non-strike frames should contain exactly %d balls and its sum should be less then %d", MAX_BALLS_COUNT, FRAME_BALLS_MAX_SUM);
}