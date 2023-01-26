package com.statista.code.challenge.domainobjects.department;

import com.statista.code.challenge.domainobjects.Booking;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class UnitedStatesDepartment extends Department {
    public UnitedStatesDepartment(UUID departmentId, String name) {
        super(departmentId, name);
    }

    private static final BigDecimal RATE_AMOUNT = BigDecimal.valueOf(12);

    @Override
    public PaymentProposal getPaymentProposal(Booking booking) {
        BigDecimal paymentRate = booking.getPrice().divide(RATE_AMOUNT, RoundingMode.UP);
        return new PaymentProposal(paymentRate, BigDecimal.ZERO, RATE_AMOUNT);
    }
}