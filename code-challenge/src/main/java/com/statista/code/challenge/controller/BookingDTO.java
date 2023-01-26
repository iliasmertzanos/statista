package com.statista.code.challenge.controller;

public record BookingDTO(String description, Double price, String currency, String subscriptionStartDate, String email,
                         String departmentName) {
}
