package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.Booking;
import org.springframework.stereotype.Repository;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    @Override
    public boolean exists(Booking booking) {
        return false;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        return null;
    }

    @Override
    public Booking createBooking(Booking booking) {
        return null;
    }
}
