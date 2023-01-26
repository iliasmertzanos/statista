package com.statista.code.challenge.service.exceptions;

public class NotValidEmailException extends RuntimeException {
    public NotValidEmailException(String message) {
        super(message);
    }
}
