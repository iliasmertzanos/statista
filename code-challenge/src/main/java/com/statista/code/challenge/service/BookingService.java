package com.statista.code.challenge.service;

import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.department.Department;

public interface BookingService {
    BookingResult createBookingAndSendEmail(BookingDTO bookingDto, Department department);
}
