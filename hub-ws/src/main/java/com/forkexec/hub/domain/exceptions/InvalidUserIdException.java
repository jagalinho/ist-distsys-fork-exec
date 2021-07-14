package com.forkexec.hub.domain.exceptions;

public class InvalidUserIdException extends Exception {
    public InvalidUserIdException() {
        super();
    }

    public InvalidUserIdException(String message) {
        super(message);
    }

    public InvalidUserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}