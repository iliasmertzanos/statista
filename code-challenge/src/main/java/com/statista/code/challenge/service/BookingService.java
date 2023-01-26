package com.statista.code.challenge.service;

import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.department.Department;

import java.util.List;

public interface BookingService {
    BookingResult createBookingAndSendEmail(BookingDTO bookingDto, Department department);

    BookingResult persistBooking(BookingDTO bookingDto, String bookingId, Department department);

    BookingResult retrieveBooking(String bookingId);

    List<BookingResult> retrieveBookings(String departmentId);
}
