package com.forkexec.rst.domain.exceptions;

public class BadMenuIdException extends Exception {
    public BadMenuIdException() {
        super();
    }

    public BadMenuIdException(String message) {
        super(message);
    }

    public BadMenuIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
