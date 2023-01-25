package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.Booking;

public interface BookingRepository {
    boolean exists(Booking booking);

    Booking updateBooking(Booking booking);

    Booking createBooking(Booking booking);
}
