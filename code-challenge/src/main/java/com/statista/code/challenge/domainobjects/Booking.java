package com.statista.code.challenge.domainobjects;

import com.statista.code.challenge.domainobjects.department.Department;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Booking {
    @NotNull
    @EqualsAndHashCode.Exclude
    private UUID bookingId;
    private String description;
    @NotNull
    @EqualsAndHashCode.Exclude
    private Double price;
    @NotNull
    private Currency currency;
    @NotNull
    private LocalDate subscriptionStartDate;
    @NotNull
    @EqualsAndHashCode.Exclude
    private Email email;
    @NotNull
    private Department department;
}
