package com.forkexec.hub.domain.exceptions;

public class InvalidFoodIdException extends Exception {
    public InvalidFoodIdException() {
        super();
    }

    public InvalidFoodIdException(String message) {
        super(message);
    }

    public InvalidFoodIdException(String message, Throwable cause) {
        super(message, cause);
    }
}