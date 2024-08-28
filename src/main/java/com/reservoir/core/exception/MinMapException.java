package com.reservoir.core.exception;



public class MinMapException extends RuntimeException {
    public MinMapException(String message) {
        super(message);
    }

    public MinMapException(String message, Throwable cause) {
        super(message, cause);
    }
}