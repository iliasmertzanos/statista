package com.statista.code.challenge.domainobjects.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Department {
    private Long departmentId;
    private String name;

    // In the code challenge requirements named as doBusiness,
    // but here named according to responsibility of this method
    public abstract String getBookingPriceToLocalCurrency(Long booking);
}