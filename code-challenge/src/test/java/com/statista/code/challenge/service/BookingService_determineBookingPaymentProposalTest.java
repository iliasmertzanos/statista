package com.statista.code.challenge.service;


import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.Email;
import com.statista.code.challenge.domainobjects.department.*;
import com.statista.code.challenge.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingService_determineBookingPaymentProposalTest {

    @InjectMocks
    private BookingServiceDefaultImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void testHappyPath_EuropeDepartment() {
        EuropeDepartment europeDepartment = new EuropeDepartment(UUID.randomUUID(), "EUROPE REPORTS");
        Booking booking = createBookingDTO(europeDepartment, 100.00);
        when(bookingRepository.findByBookingId(booking.getBookingId())).thenReturn(Optional.of(booking));

        PaymentProposal paymentProposal = bookingService.determineBookingPaymentProposal(booking.getBookingId());

        assertThat(paymentProposal.paymentRateAmount()).isEqualTo(BigDecimal.valueOf(4.17));
        assertThat(paymentProposal.ratesTotal()).isEqualTo(BigDecimal.valueOf(12));
        assertThat(paymentProposal.upfrontFee()).isEqualTo(BigDecimal.valueOf(50).setScale(2, RoundingMode.UP));
    }

    @Test
    void testHappyPath_AsiaDepartment() {
        AsiaDepartment europeDepartment = new AsiaDepartment(UUID.randomUUID(), "ASIA STATISTICS");
        Booking booking = createBookingDTO(europeDepartment, 100.00);
        when(bookingRepository.findByBookingId(booking.getBookingId())).thenReturn(Optional.of(booking));

        PaymentProposal paymentProposal = bookingService.determineBookingPaymentProposal(booking.getBookingId());

        assertThat(paymentProposal.paymentRateAmount()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(paymentProposal.ratesTotal()).isEqualTo(BigDecimal.ONE);
        assertThat(paymentProposal.upfrontFee()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void testHappyPath_UnitedStatesDepartment() {
        UnitedStatesDepartment europeDepartment = new UnitedStatesDepartment(UUID.randomUUID(), "UNITED STATES INSIGHTS");
        Booking booking = createBookingDTO(europeDepartment, 100.00);
        when(bookingRepository.findByBookingId(booking.getBookingId())).thenReturn(Optional.of(booking));

        PaymentProposal paymentProposal = bookingService.determineBookingPaymentProposal(booking.getBookingId());

        assertThat(paymentProposal.paymentRateAmount()).isEqualTo(BigDecimal.valueOf(8.4));
        assertThat(paymentProposal.ratesTotal()).isEqualTo(BigDecimal.valueOf(12));
        assertThat(paymentProposal.upfrontFee()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void testNoBookingExists() {
        when(bookingRepository.findByBookingId(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookingService.determineBookingPaymentProposal(UUID.randomUUID()));
    }

    private Booking createBookingDTO(Department department, double price) {
        return new Booking(UUID.randomUUID(), "reports", BigDecimal.valueOf(price), Currency.USD, LocalDate.now(), new Email("muesterman@gmail.com"), department);
    }
}