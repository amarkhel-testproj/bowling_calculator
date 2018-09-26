package com.amarkhel.java.model;

import lombok.Value;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public final class Game {

    private final List<Frame> frames;

    public List<Ball> getBalls() {
        return frames.stream().map(Frame::getBalls).flatMap(List::stream).collect(toList());
    }

    public List<Integer> getRolls() {
        return getBalls().stream().map(Ball::getPins).collect(toList());
    }
}