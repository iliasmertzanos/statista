package com.statista.code.challenge.domainobjects.department;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Department {
    protected UUID departmentId;
    protected String name;

    protected Department(UUID departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
    }

    // In the code challenge requirements named as doBusiness,
    // but here named according to responsibility of this method
    public abstract String getBookingPriceToLocalCurrency(Long booking);


}