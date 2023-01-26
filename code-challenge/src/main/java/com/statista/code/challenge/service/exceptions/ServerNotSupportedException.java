package com.statista.code.challenge.service.exceptions;

public class ServerNotSupportedException extends RuntimeException {
    public ServerNotSupportedException(String message) {
        super(message);
    }
}
