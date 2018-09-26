package com.amarkhel.java;

import com.amarkhel.java.model.Ball;
import com.amarkhel.java.model.Frame;
import com.amarkhel.java.model.Game;
import com.amarkhel.java.validator.Validator;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.amarkhel.java.Calculation.FRAME_BALLS_MAX_SUM;
import static java.lang.Character.*;
import static java.util.stream.Collectors.toList;
/**
  This class encapsulate all logic related to parsing and validation of input data.
  It takes 'validator' object as parameter and forward responsibility about validation to it.
  It guaranteed that Exception will be thrown when input is empty, have wrong format or didn't contain BONUS_DELIMITER(||),
  because it is very important for further algorithm. Other validation rules are enforced by 'validator' instance
  and implementation specific(it can skip validation at all or always throw error).

  Parsing algorithm:
  1) Split input by BONUS_DELIMITER, left part will represent main bowling frames and right part will represent bonus frame
  2) Parse main frames input to List of Frame objects.
  3) Try to parse bonus input(if exist one frame will be returned, wrapped as Optional or empty Optional otherwise).
  4) Combine list of balls and optional bonus frame to single list
  5) Validate constructed list, using 'validator' object
  6) If validation is successful, then new Game object will be constructed and returned as result.
     Otherwise Exception with error description will be thrown.
 */

@AllArgsConstructor
final class Parser {

    private static final String WRONG_FORMAT_ERROR = "" +
        "Input format is wrong. \n" +
        "It should have 10 frames separated by '|' and bonus frame(optionally, at the end of the input, separated by 2 preceeding '|'.\n" +
        "Each frame should contains up to 2 symbols, representing balls. Allowed symbols are: digits, 'X', '/', '-'.\n" +
        "Correct example is 'X|5/|22|--|33|X|X|--|4/|X||23'";
    private static final char MISS  = '-';
    private static final char STRIKE  = 'X';
    private static final char SPARE = '/';
    private static final String BONUS_DELIMITER = "||";
    private static final String FRAME_DELIMITER_PATTERN = "\\|";
    private static final String BONUS_DELIMITER_PATTERN = FRAME_DELIMITER_PATTERN + FRAME_DELIMITER_PATTERN;

    private final Validator validator;

    Game parse(String input) throws Exception {
        Pair<List<String>, Optional<String>> splitted = splitParts(input);
        List<String> mainInput = splitted.getKey();
        Optional<String> bonusInput = splitted.getValue();
        List<Frame> frames = combine(parseFrames(mainInput), parseBonus(bonusInput));
        Optional<String> error = validator.validate(frames);
        if (!error.isPresent()) {
            return new Game(frames);
        } else {
            throw new ParseException(error.get());
        }
    }

    private List<Frame> combine(List<Frame> frames, Optional<Frame> bonus){
        bonus.ifPresent(frames::add);
        return frames;
    }

    private List<Frame> parseFrames(List<String> input) {
        return input.stream().map(frame -> parseFrame(frame, false)).collect(toList());
    }

    private Optional<Frame> parseBonus(Optional<String> input) {
        return input.map(s -> parseFrame(s, true));
    }

    private Pair<List<String>, Optional<String>> splitParts(String input) {
        if(input == null || input.isEmpty() || !input.contains(BONUS_DELIMITER)) {
            throw new ParseException(WRONG_FORMAT_ERROR);
        }
        else {
            String[] splitted = input.split(BONUS_DELIMITER_PATTERN);
            Optional<String> bonus = splitted.length > 1 ? Optional.of(splitted[1]) : Optional.empty();
            return new Pair<>(Arrays.asList(splitted[0].split(FRAME_DELIMITER_PATTERN)), bonus);
        }
    }

    private Frame parseFrame(String input, Boolean isBonus) {
        /*
           Unfortunately there is no good equivalent of Scala's foldLeft, so intermediate state should be created implicitly.
           However, this state never escapes method boundary, so it can be counted as effectively-immutable implementation.
         */
        List<Ball> parsed = new ArrayList<>();
        for (Character character : input.toCharArray()) {
            parsed.add(parseBall(character, parsed, isBonus));
        }
        return new Frame(parsed);
    }

    private Ball parseBall(Character character, List<Ball> balls, boolean isBonus) {
        Ball ball;
        switch (character) {
            case MISS:
                ball = Ball.miss(isBonus); break;
            case STRIKE:
                ball = Ball.strike(isBonus); break;
            case SPARE: {
                ball = Ball.spare(remainingPins(balls), isBonus); break;
            }
            case '1' : case '2' : case '3' : case '4' : case '5' : case '6' : case '7' : case '8' : case '9':
                ball = Ball.usual(getNumericValue(character), isBonus); break;
            default:
                throw new ParseException(WRONG_FORMAT_ERROR);
        }
        return ball;
    }

    private int remainingPins(List<Ball> balls) {
        return FRAME_BALLS_MAX_SUM - balls.stream().mapToInt(Ball::getPins).sum();
    }
}