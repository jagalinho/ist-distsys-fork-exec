package com.forkexec.hub.domain.exceptions;

public class NotEnoughPointsException extends Exception {
    public NotEnoughPointsException() {
        super();
    }

    public NotEnoughPointsException(String message) {
        super(message);
    }

    public NotEnoughPointsException(String message, Throwable cause) {
        super(message, cause);
    }
}