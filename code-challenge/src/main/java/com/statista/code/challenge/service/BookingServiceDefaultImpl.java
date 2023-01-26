package com.statista.code.challenge.service;

import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.Email;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.repository.BookingRepository;
import com.statista.code.challenge.service.exceptions.NotValidEmailException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        Booking booking = parseBooking(bookingDto, department, Optional.empty());
        bookingRepository.createBooking(booking);
        sendEmailToCustomer(booking);
        return toBookingResult(booking);
    }

    @Override
    public BookingResult persistBooking(BookingDTO bookingDto, String bookingId, Department department) {
        Booking booking = persistBooking(bookingDto, Optional.of(bookingId), department);
        return toBookingResult(booking);
    }

    @Override
    public BookingResult retrieveBooking(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(UUID.fromString(bookingId)).orElseThrow(() -> new NoSuchElementException("The booking " + bookingId + " does not exist"));
        return toBookingResult(booking);
    }

    @Override
    public List<BookingResult> retrieveBookings(String departmentId) {
        List<Booking> bookings = bookingRepository.findBookingByDepartmentId(UUID.fromString(departmentId));
        return bookings.stream().map(this::toBookingResult).toList();
    }

    @Override
    public List<String> retrieveCurrentUsedCurrencies() {
        return bookingRepository.findAllUsedCurrencies();
    }

    @Override
    public Double retrieveBookingsTotalPriceByCurrency(String currency) {
        return bookingRepository.findBookingsByCurrency(Currency.valueOf(currency)).stream().mapToDouble(Booking::getPrice).sum();
    }

    @Override
    public Double retrieveBookingsPriceInLocalCurrency(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(UUID.fromString(bookingId)).orElseThrow(() -> new NoSuchElementException("The booking " + bookingId + " does not exist"));
        return null;
    }

    private Booking persistBooking(BookingDTO bookingDto, Optional<String> bookingId, Department department) {
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

    private BookingResult toBookingResult(Booking bookingResult) {
        LocalDate subscriptionStartDate = bookingResult.getSubscriptionStartDate();
        ZoneId zoneId = ZoneId.systemDefault();
        long subscriptionStartDateLong = subscriptionStartDate.atStartOfDay(zoneId).toEpochSecond();
        return new BookingResult(bookingResult.getDescription(), bookingResult.getPrice(), bookingResult.getCurrency().name(), Long.toString(subscriptionStartDateLong), bookingResult.getEmail().getEmailAddress(), bookingResult.getDepartment().getName());
    }

    private Booking persistBooking(Booking booking) {
        if (bookingRepository.exists(booking.getBookingId())) {
            return bookingRepository.updateBooking(booking);
        } else {
            return bookingRepository.createBooking(booking);
        }
    }

    private Booking parseBooking(BookingDTO bookingDTO, Department department, Optional<String> bookingId) {
        LocalDate subscriptionStartDate = Instant.ofEpochMilli(Long.parseLong(bookingDTO.subscriptionStartDate())).atZone(ZoneId.systemDefault()).toLocalDate();
        Booking.BookingBuilder builder = Booking.builder();
        bookingId.map(UUID::fromString).or(() -> Optional.of(UUID.randomUUID())).ifPresent(builder::bookingId);
        return builder.description(bookingDTO.description()).price(bookingDTO.price()).currency(Currency.valueOf(bookingDTO.currency())).subscriptionStartDate(subscriptionStartDate).email(new Email(bookingDTO.email())).department(department).build();
    }

    private ConfirmationDetails parseConfirmationDetails(Booking booking) {
        return new ConfirmationDetails("Booking: " + booking.getBookingId() + " was received, we will get in touch with we in a few days.", booking.getEmail().getEmailAddress());
    }
}