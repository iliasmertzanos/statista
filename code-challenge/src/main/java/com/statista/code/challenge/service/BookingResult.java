package com.statista.code.challenge.service;

import java.time.LocalDate;
import java.util.Currency;

public record BookingResult(String description, Double price, String currency, LocalDate subscriptionStartDate,
                            String email, String departmentName) {
}
