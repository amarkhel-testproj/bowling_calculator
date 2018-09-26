package com.amarkhel.java.validator;

import com.amarkhel.java.model.Ball;
import com.amarkhel.java.model.Frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.amarkhel.java.Calculation.*;

/**
 * This implementation maintains a list of independent business rules, and apply it one by one to the each frame.
 * It "exit early", when first error is encountered.
 * It returns optional value(error if exist or empty optional otherwise).
 */
public final class FullValidator implements Validator {

    private final List<Rule> rules;

    public FullValidator() {
        rules = new ArrayList<>();
        rules.add(new Rule(this::countFramesCorrect, COUNT_FRAME_ERROR));
        rules.add(new Rule(this::bonusFrameShouldExist, BONUS_FRAME_SHOULD_EXIST_ERROR));
        rules.add(new Rule(this::bonusFrameCorrect, BONUS_FRAME_INCORRECT));
        rules.add(compositeRule(this::ballsHaveCorrectPins, BALL_COUNT_PINS_ERROR));
        rules.add(compositeRule(this::ballsHaveCorrectSum, PIN_SUM_FRAME_ERROR));
        rules.add(compositeRule(this::strikeFrameCorrect, STRIKE_FRAME_INCORRECT));
        rules.add(compositeRule(this::spareFrameCorrect, SPARE_FRAME_INCORRECT));
        rules.add(compositeRule(this::usualFrameCorrect, FRAME_INCORRECT));
    }

    @Override
    public Optional<String> validate(List<Frame> frames) {
        return rules.stream().map(r -> r.run(frames)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    private boolean countFramesCorrect(List<Frame> frames) {
        return frames.stream().filter(f -> !f.isBonus()).count() == COUNT_FRAMES;
    }

    private boolean bonusFrameShouldExist(List<Frame> frames) {
        return frames.get(LAST_NOTBONUS_FRAME_INDEX).isMaxEarned() ? frames.size() == COUNT_FRAMES + 1 : true;
    }

    private boolean bonusFrameCorrect(List<Frame> frames) {
        if(frames.get(frames.size() - 1).isBonus()) {
            Frame beforeBonus = frames.get(LAST_NOTBONUS_FRAME_INDEX);
            Frame bonus = frames.get(frames.size() - 1);
            return beforeBonus.isSpare() && bonusSpareFrameCorrect(bonus) ||
                   beforeBonus.isStrike() && bonusStrikeFrameCorrect(bonus);
        } else return true;
    }

    private boolean ballsHaveCorrectPins(Frame frame) {
        return frame.getBalls().stream().allMatch(Ball::isCorrect);
    }

    private boolean ballsHaveCorrectSum(Frame frame) {
        return frame.isBonus() || frame.haveCorrectSum();
    }

    private boolean strikeFrameCorrect(Frame frame) {
        return !frame.isStrike() || frame.getBalls().size() == MAX_BALLS_COUNT - 1 && frame.getBalls().get(0).getPins() == MAXIMUM_BALL_VALUE;
    }

    private boolean spareFrameCorrect(Frame frame) {
        return !frame.isSpare() || frame.getBalls().size() == MAX_BALLS_COUNT && frame.getTotal() == FRAME_BALLS_MAX_SUM && frame.getBalls().get(0).getPins() < MAXIMUM_BALL_VALUE;
    }

    private boolean usualFrameCorrect(Frame frame) {
        return frame.isSpecial() || frame.getBalls().size() == MAX_BALLS_COUNT && frame.getTotal() < FRAME_BALLS_MAX_SUM;
    }

    private boolean bonusStrikeFrameCorrect(Frame frame) {
        return frame.getBalls().size() == MAX_BALLS_COUNT && frame.getBalls().stream().filter(Ball::isCorrect).count() == MAX_BALLS_COUNT;
    }

    private boolean bonusSpareFrameCorrect(Frame frame) {
        return frame.getBalls().size() == MAX_BALLS_COUNT - 1 && frame.getBalls().get(0).isCorrect();
    }

    class Rule {
        private final Function<List<Frame>, Boolean> op;
        private final String message;

        Rule(Function<List<Frame>, Boolean> op, String message) {
            this.op = op;
            this.message = message;
        }

        Optional<String> run(List<Frame> frames) {
            return op.apply(frames) ? Optional.empty() : Optional.of(message);
        }
    }

    private Rule compositeRule(Function<Frame, Boolean> op, String message) {
        return new Rule((frames) -> frames.stream().map(op).allMatch(b -> b == Boolean.TRUE), message);
    }
}