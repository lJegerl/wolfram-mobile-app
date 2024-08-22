package ru.ctraltdefeat.DemoWolframSingle.exception;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}