package com.statista.code.challenge.domainobjects.department;

import com.statista.code.challenge.domainobjects.Booking;

import java.math.BigDecimal;
import java.util.UUID;

public class AsiaDepartment extends Department {
    public AsiaDepartment(UUID departmentId, String name) {
        super(departmentId, name);
    }

    private static final BigDecimal RATE_AMOUNT = BigDecimal.ONE;

    @Override
    public PaymentProposal getPaymentProposal(Booking booking) {
        return new PaymentProposal(booking.getPrice(), BigDecimal.ZERO, RATE_AMOUNT);
    }
}