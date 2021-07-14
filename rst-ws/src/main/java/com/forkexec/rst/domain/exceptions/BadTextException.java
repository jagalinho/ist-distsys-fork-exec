package com.forkexec.rst.domain.exceptions;

public class BadTextException extends Exception {
    public BadTextException() {
        super();
    }

    public BadTextException(String message) {
        super(message);
    }

    public BadTextException(String message, Throwable cause) {
        super(message, cause);
    }
}
