package com.statista.code.challenge.domainobjects;

import com.statista.code.challenge.domainobjects.department.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DepartmentTest {

    @Test
    void testEuropeDepartment() {
        EuropeDepartment europeDepartment = new EuropeDepartment(UUID.randomUUID(), "DEPARTMENT NAME");
        Booking booking = getBooking();
        PaymentProposal paymentProposal = europeDepartment.getPaymentProposal(booking);
        assertThat(paymentProposal.paymentRateAmount()).isEqualTo(BigDecimal.valueOf(4.17));
        assertThat(paymentProposal.ratesTotal()).isEqualTo(BigDecimal.valueOf(12));
        assertThat(paymentProposal.upfrontFee()).isEqualTo(BigDecimal.valueOf(50).setScale(2, RoundingMode.UP));
    }

    @Test
    void testAsiaDepartment() {
        AsiaDepartment asiaDepartment = new AsiaDepartment(UUID.randomUUID(), "DEPARTMENT NAME");
        Booking booking = getBooking();
        PaymentProposal paymentProposal = asiaDepartment.getPaymentProposal(booking);
        assertThat(paymentProposal.paymentRateAmount()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(paymentProposal.ratesTotal()).isEqualTo(BigDecimal.ONE);
        assertThat(paymentProposal.upfrontFee()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void testUnitedStatesDepartment() {
        UnitedStatesDepartment unitedStatesDepartment = new UnitedStatesDepartment(UUID.randomUUID(), "DEPARTMENT NAME");
        Booking booking = getBooking();
        PaymentProposal paymentProposal = unitedStatesDepartment.getPaymentProposal(booking);
        assertThat(paymentProposal.paymentRateAmount()).isEqualTo(BigDecimal.valueOf(8.4));
        assertThat(paymentProposal.ratesTotal()).isEqualTo(BigDecimal.valueOf(12));
        assertThat(paymentProposal.upfrontFee()).isEqualTo(BigDecimal.ZERO);
    }

    private static Booking getBooking() {
        return Booking.builder()
                .bookingId(UUID.randomUUID())
                .price(BigDecimal.valueOf(100.00))
                .email(new Email(""))
                .subscriptionStartDate(LocalDate.now())
                .currency(Currency.EURO)
                .department(mock(Department.class))
                .build();
    }
}