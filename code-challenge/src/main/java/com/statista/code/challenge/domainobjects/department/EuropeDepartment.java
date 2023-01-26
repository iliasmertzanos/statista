package com.statista.code.challenge.domainobjects.department;

import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;

import java.util.UUID;

public class EuropeDepartment extends Department {
    public EuropeDepartment(UUID departmentId, String name) {
        super(departmentId, name);
    }

    @Override
    public String getBookingInformationInLocalLanguage(Booking booking) {
        return null;
    }
}
