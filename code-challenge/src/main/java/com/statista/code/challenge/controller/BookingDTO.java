package com.statista.code.challenge.controller;

import com.statista.code.challenge.domainobjects.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingDTO(String description, BigDecimal price, Currency currency, LocalDate subscriptionStartDate,
                         String email, String departmentName) {
}
