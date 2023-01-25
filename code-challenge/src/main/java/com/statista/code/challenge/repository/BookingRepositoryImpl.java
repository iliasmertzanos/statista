package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.Booking;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private final Set<Booking> bookings = new HashSet<>();

    @Override
    public boolean exists(Booking booking) {
        return bookings.stream().anyMatch(booking::equals);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        bookings.stream().filter(booking::equals).findFirst().ifPresent(currentBooking -> {
            currentBooking.setDepartment(booking.getDepartment());
            currentBooking.setCurrency(booking.getCurrency());
            currentBooking.setPrice(booking.getPrice());
            currentBooking.setEmail(booking.getEmail());
            currentBooking.setSubscriptionStartDate(booking.getSubscriptionStartDate());
        });
        return booking;
    }

    @Override
    public Booking createBooking(Booking booking) {
        bookings.add(booking);
        return booking;
    }
}
