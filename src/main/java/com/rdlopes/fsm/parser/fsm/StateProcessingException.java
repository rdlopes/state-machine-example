package com.rdlopes.fsm.parser.fsm;

public class StateProcessingException extends Exception {
    public StateProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateProcessingException(String message) {
        super(message);
    }
}
