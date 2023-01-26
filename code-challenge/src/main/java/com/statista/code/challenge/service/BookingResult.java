package com.statista.code.challenge.service;

public record BookingResult(String description, Double price, String currency, String subscriptionStartDate,
                            String email, String departmentName) {
}
