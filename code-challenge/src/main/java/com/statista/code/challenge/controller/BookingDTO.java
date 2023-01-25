package com.statista.code.challenge.controller;

import java.time.LocalDate;
import java.util.Currency;

public record BookingDTO(String description, Double price, String currency, LocalDate subscriptionStartDate,
                         String email, String departmentName) {
}
