package com.statista.code.challenge.service;

import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.PaymentProposal;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingResult createBookingAndSendEmail(BookingDTO bookingDto, Department department);

    BookingResult persistBooking(BookingDTO bookingDto, UUID bookingId, Department department);

    BookingResult retrieveBooking(UUID bookingId);

    List<BookingResult> retrieveBookings(UUID departmentId);

    List<String> retrieveCurrentUsedCurrencies();

    BigDecimal retrieveBookingsTotalPriceByCurrency(Currency currency);

    PaymentProposal determineBookingPaymentProposal(UUID bookingId);
}