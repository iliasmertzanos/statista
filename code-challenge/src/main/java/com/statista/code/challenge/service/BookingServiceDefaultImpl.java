package com.statista.code.challenge.service;

import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.Email;
import com.statista.code.challenge.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceDefaultImpl implements BookingService {
    private final BookingRepository bookingRepository;

    public BookingServiceDefaultImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingResult createBookingAndSendEmail(BookingDTO bookingDto, Department department) {
        Booking booking = parseBooking(bookingDto, department);
        Booking bookingResult = persisteBooking(booking);
        return toBookingResult(bookingResult);
    }

    private BookingResult toBookingResult(Booking bookingResult) {
        return new BookingResult(bookingResult.getDescription(), bookingResult.getPrice(), bookingResult.getCurrency(), bookingResult.getSubscriptionStartDate(), bookingResult.getEmail().toString(), bookingResult.getDepartment().toString());
    }

    private Booking persisteBooking(Booking booking) {
        if (bookingRepository.exists(booking)) {
            return bookingRepository.createBooking(booking);
        } else {
            return bookingRepository.updateBooking(booking);
        }
    }

    public Booking parseBooking(BookingDTO bookingDTO, Department department) {
        return Booking.builder().description(bookingDTO.description()).price(bookingDTO.price()).currency(bookingDTO.currency()).subscriptionStartDate(bookingDTO.subscriptionStartDate()).email(new Email(bookingDTO.email())).department(department).build();
    }
}
