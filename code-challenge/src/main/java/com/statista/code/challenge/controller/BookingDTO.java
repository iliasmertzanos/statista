package com.statista.code.challenge.controller;

import com.statista.code.challenge.domainobjects.Email;

import java.time.LocalDate;
import java.util.Currency;

public record BookingDTO(String description, Double price, Currency currency, LocalDate subscriptionStartDate,
                         String email, String departmentRegion) {
}
