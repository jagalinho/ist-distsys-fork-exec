package com.forkexec.hub.domain.exceptions;

public class EmptyCartException extends Exception {
    public EmptyCartException() {
        super();
    }

    public EmptyCartException(String message) {
        super(message);
    }

    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }
}