package com.forkexec.rst.domain.exceptions;

public class InsufficientQuantityException extends Exception {
    public InsufficientQuantityException() {
        super();
    }

    public InsufficientQuantityException(String message) {
        super(message);
    }

    public InsufficientQuantityException(String message, Throwable cause) {
        super(message, cause);
    }
}
