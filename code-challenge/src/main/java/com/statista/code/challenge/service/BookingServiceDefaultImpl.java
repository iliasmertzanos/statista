package com.statista.code.challenge.service;

import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.Email;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingServiceDefaultImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    public BookingServiceDefaultImpl(BookingRepository bookingRepository, NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
    }

    @Override
    public BookingResult createBookingAndSendEmail(BookingDTO bookingDto, Department department) {
        Booking booking = parseBooking(bookingDto, department);
        persisteBooking(booking);
        sendEmailToCustomer(booking);
        return toBookingResult(booking);
    }

    private void sendEmailToCustomer(Booking booking) {
        if (!booking.getEmail().isValid()) {
            throw new NotValidEmailException("The email address: " + booking.getEmail().getEmailAddress() + " ist not valid.");
        }
        ConfirmationDetails confirmationDetails = parseConfirmationDetails(booking);
        notificationService.sendBookingConfirmation(confirmationDetails);
    }

    private BookingResult toBookingResult(Booking bookingResult) {
        return new BookingResult(bookingResult.getDescription(), bookingResult.getPrice(), bookingResult.getCurrency().name(), bookingResult.getSubscriptionStartDate(), bookingResult.getEmail().getEmailAddress(), bookingResult.getDepartment().getName());
    }

    private void persisteBooking(Booking booking) {
        if (bookingRepository.exists(booking)) {
            bookingRepository.createBooking(booking);
        } else {
            bookingRepository.updateBooking(booking);
        }
    }

    public Booking parseBooking(BookingDTO bookingDTO, Department department) {
        return Booking.builder().bookingId(UUID.randomUUID()).description(bookingDTO.description()).price(bookingDTO.price()).currency(Currency.valueOf(bookingDTO.currency())).subscriptionStartDate(bookingDTO.subscriptionStartDate()).email(new Email(bookingDTO.email())).department(department).build();
    }

    public ConfirmationDetails parseConfirmationDetails(Booking booking) {
        return new ConfirmationDetails("Booking: " + booking.getBookingId() + " was received, we will get in touch with we in a few days.", booking.getEmail().getEmailAddress());
    }
}
