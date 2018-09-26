package com.amarkhel.java;

import com.amarkhel.java.validator.FullValidator;
import com.amarkhel.java.validator.NoOpValidator;
import com.amarkhel.java.validator.Validator;

/**
 * This class playing role as entry-point of application.
 * It takes validator and calculation objects as parameters, and use it for input validation and score calculation accordingly.
 * It provides the list of pre-defined implementations, composed as building-blocks, using different implementations of validator and calculation.
 * 1) fp instance - use FP calculation algorithm and rule-based validator
 * 2) fpNoValidator instance - use FP calculation algorithm and no-op validator
 * 3) classic instance - use classic imperative calculation algorithm and rule-based validator
 * 4) classicNoValidator instance - use classic imperative calculation algorithm and no-op validator
 */
public final class GameService {

    //In real project will be replaced by DI framework
    public static final GameService classic = new GameService(new FullValidator(), new ClassicCalculation());
    public static final GameService classicNoValidator = new GameService(new NoOpValidator(), new ClassicCalculation());
    public static final GameService fpNoValidator = new GameService(new NoOpValidator(), new FPCalculation());
    public static final GameService fp = new GameService(new FullValidator(), new FPCalculation());

    private final Calculation calculation;
    private final Parser parser;

    private GameService(Validator validator, Calculation calculation){
        this.calculation = calculation;
        parser = new Parser(validator);
    }

    public int calculateScore(String input) throws Exception {
        return calculation.score(parser.parse(input));
    }
}