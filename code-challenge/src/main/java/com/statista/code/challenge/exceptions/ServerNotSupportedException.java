package com.statista.code.challenge.exceptions;

public class ServerNotSupportedException extends RuntimeException {
    public ServerNotSupportedException(String message) {
        super(message);
    }
}