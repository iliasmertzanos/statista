package com.statista.code.challenge.service;

public class NotValidEmailException extends RuntimeException {
    public NotValidEmailException(String message) {
        super(message);
    }
}
