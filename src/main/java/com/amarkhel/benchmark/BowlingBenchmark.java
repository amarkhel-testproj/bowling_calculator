package com.amarkhel.benchmark;

import java.util.concurrent.TimeUnit;

import com.amarkhel.*;
import com.amarkhel.java.GameService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

/**
 * This benchmark is used to calculate performance of each combination of(calculation algorithm + validator) in both Java and Scala implementations.
 */
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
public class BowlingBenchmark {
    @Param({ "10000" })
    private int size;

    private String[] samples;

    @Setup
    public void prepare() {
        samples = new String[]{"11|11|11|11|11|11|11|11|11|11||", "X|X|X|X|X|X|X|X|X|X||--", "X|X|X|X|X|X|X|X|X|X||11", "9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||", "X|X|X|X|X|X|X|X|X|X||XX"};
    }

    @Benchmark
    public void fpNoValidatorJava(Blackhole fox) throws Exception {
        for (int y = 0; y < size; y++) {
            fox.consume(GameService.fpNoValidator.calculateScore(samples[y % samples.length]));
        }
    }

    @Benchmark
    public void fpValidatorJava(Blackhole fox) throws Exception {
        for (int y = 0; y < size; y++) {
            fox.consume(GameService.fp.calculateScore(samples[y % samples.length]));
        }
    }

    @Benchmark
    public void classicNoValidatorJava(Blackhole fox) throws Exception {
        for (int y = 0; y < size; y++) {
            fox.consume(GameService.classicNoValidator.calculateScore(samples[y % samples.length]));
        }
    }

    @Benchmark
    public void classicValidatorJava(Blackhole fox) throws Exception {
        for (int y = 0; y < size; y++) {
            fox.consume(GameService.classic.calculateScore(samples[y % samples.length]));
        }
    }

    @Benchmark
    public void classicNoValidator(Blackhole fox) {
        for (int y = 0; y < size; y++) {
            fox.consume(ClassicGameServiceNoValidator$.MODULE$.calculateScore(samples[y % samples.length]));
        }
    }

    @Benchmark
    public void classicValidator(Blackhole fox) {
        for (int y = 0; y < size; y++) {
            fox.consume(ClassicGameService$.MODULE$.calculateScore(samples[y % samples.length]));
        }
    }

    @Benchmark
    public void fpNoValidator(Blackhole fox) {
        for (int y = 0; y < size; y++) {
            fox.consume(FPGameServiceNoValidator$.MODULE$.calculateScore(samples[y % samples.length]));
        }
    }

    @Benchmark
    public void fpValidator(Blackhole fox) {
        for (int y = 0; y < size; y++) {
            fox.consume(FPGameService$.MODULE$.calculateScore(samples[y % samples.length]));
        }
    }
}
