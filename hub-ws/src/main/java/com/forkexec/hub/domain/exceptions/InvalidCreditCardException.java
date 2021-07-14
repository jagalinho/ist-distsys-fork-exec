package com.forkexec.hub.domain.exceptions;

public class InvalidCreditCardException extends Exception {
    public InvalidCreditCardException() {
        super();
    }

    public InvalidCreditCardException(String message) {
        super(message);
    }

    public InvalidCreditCardException(String message, Throwable cause) {
        super(message, cause);
    }
}