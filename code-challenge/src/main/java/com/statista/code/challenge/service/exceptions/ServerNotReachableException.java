package com.statista.code.challenge.service.exceptions;

public class ServerNotReachableException extends RuntimeException {
    public ServerNotReachableException(String message) {
        super(message);
    }
}
