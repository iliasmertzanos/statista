package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BookingRepositoryImpl implements BookingRepository {
    private final Set<Booking> bookings = new HashSet<>();

    @Override
    public boolean exists(UUID bookingId) {
        return bookings.stream().anyMatch(booking -> findByBookingId(bookingId).isPresent());
    }

    @Override
    public Booking updateBooking(Booking booking) {
        findByBookingId(booking.getBookingId()).ifPresent(currentBooking -> {
            currentBooking.setDescription(booking.getDescription());
            currentBooking.setDepartment(booking.getDepartment());
            currentBooking.setCurrency(booking.getCurrency());
            currentBooking.setPrice(booking.getPrice());
            currentBooking.setEmail(booking.getEmail());
            currentBooking.setSubscriptionStartDate(booking.getSubscriptionStartDate());
        });
        log.info("update bookings: " + bookings.stream().map(Booking::toString).collect(Collectors.joining(",")));
        return booking;
    }

    @Override
    public Optional<Booking> findByBookingId(UUID bookingId) {
        return bookings.stream().filter(booking -> bookingId.equals(booking.getBookingId())).findFirst();
    }

    @Override
    public Booking createBooking(Booking booking) {
        bookings.add(booking);
        log.info("create bookings: " + bookings.stream().map(Booking::toString).collect(Collectors.joining(",/n")));
        return booking;
    }

    @Override
    public List<Booking> findBookingByDepartmentId(UUID departmentId) {
        return bookings.stream().filter(booking -> booking.getDepartment().getDepartmentId().equals(departmentId)).toList();
    }
}