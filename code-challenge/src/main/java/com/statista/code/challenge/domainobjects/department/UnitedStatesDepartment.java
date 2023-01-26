package com.statista.code.challenge.domainobjects.department;

import com.statista.code.challenge.domainobjects.Booking;

import java.util.UUID;

public class UnitedStatesDepartment extends Department {
    public UnitedStatesDepartment(UUID departmentId, String name) {
        super(departmentId, name);
    }

    @Override
    public String getBookingInformationInLocalLanguage(Booking booking) {
        return null;
    }
}
