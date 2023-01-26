package com.statista.code.challenge.exceptions;

public class ServerNotReachableException extends RuntimeException {
    public ServerNotReachableException(String message) {
        super(message);
    }
}
