package com.forkexec.hub.domain.exceptions;

public class UddiLookupException extends RuntimeException {
    public UddiLookupException() {
        super();
    }

    public UddiLookupException(String message) {
        super(message);
    }

    public UddiLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}