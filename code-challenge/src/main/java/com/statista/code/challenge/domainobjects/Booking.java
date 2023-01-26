package com.statista.code.challenge.domainobjects;

import com.statista.code.challenge.domainobjects.department.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Booking {
    @NotNull
    private UUID bookingId;
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

    public String getPriceToLocalCurrency() {
        return department.getBookingInformationInLocalLanguage(this);
    }
}
