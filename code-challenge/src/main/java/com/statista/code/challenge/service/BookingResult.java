package com.statista.code.challenge.service;

import com.statista.code.challenge.domainobjects.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingResult(String description, BigDecimal price, Currency currency, LocalDate subscriptionStartDate,
                            String email, String departmentName) {
}
