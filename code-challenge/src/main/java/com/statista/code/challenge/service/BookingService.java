package com.statista.code.challenge.service;

import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.PaymentProposal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface BookingService {
    BookingResult createBookingAndNotify(BookingDTO bookingDto, Department department);

    BookingResult persistBooking(BookingDTO bookingDto, UUID bookingId, Department department);

    BookingResult retrieveBooking(UUID bookingId);

    List<BookingResult> retrieveBookings(UUID departmentId);

    Set<String> retrieveCurrentUsedCurrencies();

    BigDecimal retrieveBookingsTotalPriceByCurrency(Currency currency);

    PaymentProposal determineBookingPaymentProposal(UUID bookingId);
}