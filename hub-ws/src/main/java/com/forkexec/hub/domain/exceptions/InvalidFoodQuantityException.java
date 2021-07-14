package com.forkexec.hub.domain.exceptions;

public class InvalidFoodQuantityException extends Exception {
    public InvalidFoodQuantityException() {
        super();
    }

    public InvalidFoodQuantityException(String message) {
        super(message);
    }

    public InvalidFoodQuantityException(String message, Throwable cause) {
        super(message, cause);
    }
}