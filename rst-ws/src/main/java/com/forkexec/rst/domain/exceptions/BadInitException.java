package com.forkexec.rst.domain.exceptions;

public class BadInitException extends Exception {
    public BadInitException() {
        super();
    }

    public BadInitException(String message) {
        super(message);
    }

    public BadInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
