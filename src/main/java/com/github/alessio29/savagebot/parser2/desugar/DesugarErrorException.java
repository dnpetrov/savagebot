package com.github.alessio29.savagebot.parser2.desugar;

public class DesugarErrorException extends RuntimeException {
    public DesugarErrorException(String message) {
        super(message);
    }

    public DesugarErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
