package com.forkexec.hub.domain.exceptions;

public class InvalidTextException extends Exception {
    public InvalidTextException() {
        super();
    }

    public InvalidTextException(String message) {
        super(message);
    }

    public InvalidTextException(String message, Throwable cause) {
        super(message, cause);
    }
}