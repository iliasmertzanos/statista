package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BookingRepository {
    boolean exists(UUID bookingId);

    Booking updateBooking(Booking booking);

    Optional<Booking> findByBookingId(UUID bookingId);

    Booking createBooking(Booking booking);

    List<Booking> findBookingByDepartmentId(UUID departmentId);

    Set<String> findAllUsedCurrencies();

    List<Booking> findBookingsByCurrency(Currency currency);
}