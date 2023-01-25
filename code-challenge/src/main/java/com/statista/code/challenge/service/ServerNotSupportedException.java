package com.statista.code.challenge.service;

public class ServerNotSupportedException extends RuntimeException {
    public ServerNotSupportedException(String message) {
        super(message);
    }
}
