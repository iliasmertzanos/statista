package com.statista.code.challenge.controller;

import java.math.BigDecimal;

public record BookingDTO(String description, BigDecimal price, String currency, String subscriptionStartDate,
                         String email, String departmentName) {
}
