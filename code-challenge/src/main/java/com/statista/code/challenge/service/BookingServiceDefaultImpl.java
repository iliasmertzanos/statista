package com.statista.code.challenge.service;

import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.Email;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.PaymentProposal;
import com.statista.code.challenge.exceptions.NotValidEmailException;
import com.statista.code.challenge.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
        Booking booking = parseBooking(bookingDto, department, Optional.empty());
        Booking persistedBooking = bookingRepository.createBooking(booking);
        sendEmailToCustomer(persistedBooking);
        return toBookingResult(persistedBooking);
    }

    @Override
    public BookingResult persistBooking(BookingDTO bookingDto, UUID bookingId, Department department) {
        Booking booking = persistBooking(bookingDto, Optional.of(bookingId), department);
        return toBookingResult(booking);
    }

    @Override
    public BookingResult retrieveBooking(UUID bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId).orElseThrow(() -> new NoSuchElementException("The booking " + bookingId + " does not exist"));
        return toBookingResult(booking);
    }

    @Override
    public List<BookingResult> retrieveBookings(UUID departmentId) {
        List<Booking> bookings = bookingRepository.findBookingByDepartmentId(departmentId);
        return bookings.stream().map(this::toBookingResult).toList();
    }

    @Override
    public Set<String> retrieveCurrentUsedCurrencies() {
        return bookingRepository.findAllUsedCurrencies();
    }

    @Override
    public BigDecimal retrieveBookingsTotalPriceByCurrency(Currency currency) {
        return bookingRepository.findBookingsByCurrency(currency)
                .stream().map(Booking::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.UP);
    }

    @Override
    //doBusiness
    public PaymentProposal determineBookingPaymentProposal(UUID bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId).orElseThrow(() -> new NoSuchElementException("The booking " + bookingId + " does not exist"));
        return booking.getPaymentProposal();
    }

    private Booking persistBooking(BookingDTO bookingDto, Optional<UUID> bookingId, Department department) {
        Booking booking = parseBooking(bookingDto, department, bookingId);
        return persistBooking(booking);
    }

    private void sendEmailToCustomer(Booking booking) {
        if (!booking.getEmail().isValid()) {
            throw new NotValidEmailException("The email address: " + booking.getEmail().getEmailAddress() + " ist not valid.");
        }
        ConfirmationDetails confirmationDetails = parseConfirmationDetails(booking);
        notificationService.sendBookingConfirmation(confirmationDetails);
    }

    private BookingResult toBookingResult(Booking booking) {
        return new BookingResult(booking.getDescription(),
                booking.getPrice(),
                booking.getCurrency(),
                booking.getSubscriptionStartDate(),
                booking.getEmail().getEmailAddress(),
                booking.getDepartment().getName());
    }

    private Booking persistBooking(Booking booking) {
        if (bookingRepository.exists(booking.getBookingId())) {
            return bookingRepository.updateBooking(booking);
        } else {
            return bookingRepository.createBooking(booking);
        }
    }

    private Booking parseBooking(BookingDTO bookingDTO, Department department, Optional<UUID> optionalBookingId) {
        Booking.BookingBuilder builder = Booking.builder();
        UUID bookingId = optionalBookingId.orElse(UUID.randomUUID());
        BigDecimal price = bookingDTO.price().setScale(2, RoundingMode.UP);
        return builder.bookingId(bookingId)
                .description(bookingDTO.description())
                .price(price)
                .currency(bookingDTO.currency())
                .subscriptionStartDate(bookingDTO.subscriptionStartDate())
                .email(new Email(bookingDTO.email()))
                .department(department).build();
    }

    private ConfirmationDetails parseConfirmationDetails(Booking booking) {
        return new ConfirmationDetails("Booking: " + booking.getBookingId() + " was received, we will get in touch with we in a few days.", booking.getEmail().getEmailAddress());
    }
}