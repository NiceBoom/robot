package com.fly.robot.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
