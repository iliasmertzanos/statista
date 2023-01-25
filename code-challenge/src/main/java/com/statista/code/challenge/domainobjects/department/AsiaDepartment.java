package com.statista.code.challenge.domainobjects.department;

import java.util.UUID;

public class AsiaDepartment extends Department {
    public AsiaDepartment(UUID departmentId, String name) {
        super(departmentId, name);
    }

    @Override
    public String getBookingPriceToLocalCurrency(Long booking) {
        return null;
    }
}
