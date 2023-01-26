package com.statista.code.challenge.service;


import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.Email;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.EuropeDepartment;
import com.statista.code.challenge.exceptions.NotValidEmailException;
import com.statista.code.challenge.exceptions.ServerNotReachableException;
import com.statista.code.challenge.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingService_createBookingAndSendEmailTest {

    @InjectMocks
    private BookingServiceDefaultImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private NotificationService notificationService;

    @Test
    void testHappyPath() {
        ZoneId zoneId = ZoneId.systemDefault();
        long subscriptionStartDateLong = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        BookingDTO bookingDTO = new BookingDTO("reports", BigDecimal.valueOf(25.00), Currency.USD.name(), Long.toString(subscriptionStartDateLong), "muesterman@gmail.com", "EUROPE REPORTS");
        EuropeDepartment europeDepartment = new EuropeDepartment(UUID.randomUUID(), "EUROPE REPORTS");
        Booking booking = parseBooking(bookingDTO, europeDepartment);
        when(bookingRepository.createBooking(any())).thenReturn(booking);
        BookingResult bookingResult = bookingService.createBookingAndSendEmail(bookingDTO, europeDepartment);

        assertThat(bookingResult.description()).isEqualTo(booking.getDescription());
        assertThat(bookingResult.email()).isEqualTo(booking.getEmail().getEmailAddress());
        assertThat(bookingResult.currency()).isEqualTo(booking.getCurrency().name());
        assertThat(bookingResult.price()).isEqualTo(booking.getPrice());
        assertThat(bookingResult.departmentName()).isEqualTo(booking.getDepartment().getName());

        ArgumentCaptor<ConfirmationDetails> confirmationDetailsArgumentCaptor = ArgumentCaptor.forClass(ConfirmationDetails.class);
        verify(notificationService, Mockito.times(1)).sendBookingConfirmation(confirmationDetailsArgumentCaptor.capture());
        List<ConfirmationDetails> confirmationDetailsArguments = confirmationDetailsArgumentCaptor.getAllValues().stream().toList();
        assertThat(confirmationDetailsArguments).hasSize(1);
        ConfirmationDetails confirmationDetails = confirmationDetailsArguments.get(0);
        assertThat(confirmationDetails.emailAddress()).isEqualTo(booking.getEmail().getEmailAddress());
    }

    @Test
    void testEmailIsNotValid() {
        ZoneId zoneId = ZoneId.systemDefault();
        long subscriptionStartDateLong = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        BookingDTO bookingDTO = new BookingDTO("reports", BigDecimal.valueOf(25.00), Currency.USD.name(), Long.toString(subscriptionStartDateLong), "NOT_VALID_EMAIL.mail.com", "EUROPE REPORTS");
        EuropeDepartment europeDepartment = new EuropeDepartment(UUID.randomUUID(), "EUROPE REPORTS");
        Booking booking = parseBooking(bookingDTO, europeDepartment);
        when(bookingRepository.createBooking(any())).thenReturn(booking);
        assertThrows(NotValidEmailException.class, () -> bookingService.createBookingAndSendEmail(bookingDTO, europeDepartment));
    }

    private Booking parseBooking(BookingDTO bookingDTO, Department department) {
        LocalDate subscriptionStartDate = Instant.ofEpochMilli(Long.parseLong(bookingDTO.subscriptionStartDate())).atZone(ZoneId.systemDefault()).toLocalDate();
        Booking.BookingBuilder builder = Booking.builder();
        BigDecimal price = bookingDTO.price().setScale(2, RoundingMode.UP);
        return builder.bookingId(UUID.randomUUID()).description(bookingDTO.description()).price(price).currency(Currency.valueOf(bookingDTO.currency())).subscriptionStartDate(subscriptionStartDate).email(new Email(bookingDTO.email())).department(department).build();
    }
}