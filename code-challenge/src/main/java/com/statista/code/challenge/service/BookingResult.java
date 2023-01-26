package com.statista.code.challenge.service;

import java.math.BigDecimal;

public record BookingResult(String description, BigDecimal price, String currency, String subscriptionStartDate,
                            String email, String departmentName) {
}
