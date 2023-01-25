package com.statista.code.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookingservice")
public class BookingController {

    @PostMapping("/booking")
    public ResponseEntity createBooking() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/booking/{transactionId}")
    public ResponseEntity updateBooking() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity getBookingById() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/booking/type/{type}")
    public ResponseEntity getBookingsOfType() {
        return ResponseEntity.ok().build();
    }
}