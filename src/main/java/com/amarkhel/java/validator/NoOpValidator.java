package com.amarkhel.java.validator;

import com.amarkhel.java.model.Frame;

import java.util.List;
import java.util.Optional;
/**
 * This implementation always return true, i.e not validate input at all.
 */
public final class NoOpValidator implements Validator {
    @Override
    public Optional<String> validate(List<Frame> frames) {
        return Optional.empty();
    }
}