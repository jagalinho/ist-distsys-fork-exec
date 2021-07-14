package com.forkexec.rst.domain.exceptions;

public class BadQuantityException extends Exception {
    public BadQuantityException() {
        super();
    }

    public BadQuantityException(String message) {
        super(message);
    }

    public BadQuantityException(String message, Throwable cause) {
        super(message, cause);
    }
}
