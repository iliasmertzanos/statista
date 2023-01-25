package com.statista.code.challenge.service;

public class ServerNotReachableException extends RuntimeException {
    public ServerNotReachableException(String message) {
        super(message);
    }
}
