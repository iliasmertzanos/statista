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
    public BigDecimal retrieveBookingsTotalPriceByCurrency(String currency) {
        return bookingRepository.findBookingsByCurrency(Currency.valueOf(currency)).stream().map(Booking::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.UP);
    }

    @Override
    public PaymentProposal determineBookingPaymentProposal(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(UUID.fromString(bookingId)).orElseThrow(() -> new NoSuchElementException("The booking " + bookingId + " does not exist"));
        return booking.getPaymentProposal();
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

    private BookingResult toBookingResult(Booking booking) {
        LocalDate subscriptionStartDate = booking.getSubscriptionStartDate();
        ZoneId zoneId = ZoneId.systemDefault();
        long subscriptionStartDateLong = subscriptionStartDate.atStartOfDay(zoneId).toEpochSecond();
        return new BookingResult(booking.getDescription(), booking.getPrice(), booking.getCurrency().name(), Long.toString(subscriptionStartDateLong), booking.getEmail().getEmailAddress(), booking.getDepartment().getName());
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
        BigDecimal price = bookingDTO.price().setScale(2, RoundingMode.UP);
        return builder.description(bookingDTO.description()).price(price).currency(Currency.valueOf(bookingDTO.currency())).subscriptionStartDate(subscriptionStartDate).email(new Email(bookingDTO.email())).department(department).build();
    }

    private ConfirmationDetails parseConfirmationDetails(Booking booking) {
        return new ConfirmationDetails("Booking: " + booking.getBookingId() + " was received, we will get in touch with we in a few days.", booking.getEmail().getEmailAddress());
    }
}