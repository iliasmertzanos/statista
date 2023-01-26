package com.statista.code.challenge.domainobjects.department;

import com.statista.code.challenge.domainobjects.Booking;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class EuropeDepartment extends Department {
    public EuropeDepartment(UUID departmentId, String name) {
        super(departmentId, name);
    }

    private static final BigDecimal RATE_AMOUNT = BigDecimal.valueOf(12);
    private static final BigDecimal UPFRONT_PAYMENT_RATE = BigDecimal.valueOf(0.5);

    @Override
    public PaymentProposal getPaymentProposal(Booking booking) {
        BigDecimal bookingPrice = booking.getPrice();
        BigDecimal upfrontFee = booking.getPrice().multiply(UPFRONT_PAYMENT_RATE);
        BigDecimal endPrice = bookingPrice.subtract(upfrontFee);
        BigDecimal paymentRate = endPrice.divide(RATE_AMOUNT, RoundingMode.UP);
        return new PaymentProposal(paymentRate, upfrontFee, RATE_AMOUNT);
    }
}