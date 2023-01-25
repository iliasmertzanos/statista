package com.statista.code.challenge.domainobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Currency;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @NotNull
    private Long bookingId;
    private String description;
    @NotNull
    private Double price;
    @NotNull
    private Currency currency;
    @NotNull
    private LocalDate subscriptionStartDate;
    @NotNull
    private Email email;
    @NotNull
    private Department department;
}
