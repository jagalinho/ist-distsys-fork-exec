package com.forkexec.hub.domain.exceptions;

public class InvalidInitException extends Exception {
    public InvalidInitException() {
        super();
    }

    public InvalidInitException(String message) {
        super(message);
    }

    public InvalidInitException(String message, Throwable cause) {
        super(message, cause);
    }
}