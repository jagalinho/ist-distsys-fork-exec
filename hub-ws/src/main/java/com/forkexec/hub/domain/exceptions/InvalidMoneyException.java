package com.forkexec.hub.domain.exceptions;

public class InvalidMoneyException extends Exception {
    public InvalidMoneyException() {
        super();
    }

    public InvalidMoneyException(String message) {
        super(message);
    }

    public InvalidMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}