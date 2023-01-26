package com.statista.code.challenge.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class BookingExceptionHandler {
    @ExceptionHandler({NotValidEmailException.class, ServerNotSupportedException.class})
    public ResponseEntity<String> notificationExceptionHandler(RuntimeException e) {
        JSONObject response = new JSONObject();
        response.put("stackTrace", e.getStackTrace());
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response.toString(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(ServerNotReachableException.class)
    public ResponseEntity<String> serverExceptionHandler(RuntimeException e) {
        JSONObject response = new JSONObject();
        response.put("stackTrace", e.getStackTrace());
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response.toString(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> runtimeExceptionHandler(RuntimeException e) {
        JSONObject response = new JSONObject();
        response.put("stackTrace", e.getStackTrace());
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response.toString(), HttpStatus.NOT_FOUND);
    }
}