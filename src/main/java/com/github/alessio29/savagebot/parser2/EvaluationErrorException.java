package com.github.alessio29.savagebot.parser2;

public class EvaluationErrorException extends RuntimeException {
    public EvaluationErrorException(String message) {
        super(message);
    }

    public EvaluationErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
