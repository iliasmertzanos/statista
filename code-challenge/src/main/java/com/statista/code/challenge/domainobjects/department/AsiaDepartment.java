package com.statista.code.challenge.domainobjects.department;

import com.statista.code.challenge.domainobjects.Booking;

import java.util.UUID;

public class AsiaDepartment extends Department {
    public AsiaDepartment(UUID departmentId, String name) {
        super(departmentId, name);
    }

    @Override
    public String getBookingInformationInLocalLanguage(Booking booking) {
        return "元越何月明字。當上當風要遊。書應案列邊照業一力樹原活有 Booking: " + booking.getBookingId() + " 資生備要府能車進發年還備行汽快一著買種作客的片，Price: " + booking.getPrice() + booking.getCurrency().getSymbol() + " 資生備要府能車進發年還備 " + booking.getDescription();
    }
}


