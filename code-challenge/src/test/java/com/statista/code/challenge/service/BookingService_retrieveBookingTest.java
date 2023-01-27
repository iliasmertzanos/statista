package com.statista.code.challenge.service;


import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.Email;
import com.statista.code.challenge.domainobjects.department.EuropeDepartment;
import com.statista.code.challenge.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingService_retrieveBookingTest {

    @InjectMocks
    private BookingServiceDefaultImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void testHappyPath() {
        UUID bookingId = UUID.randomUUID();
        Booking booking = createBookingDTO(bookingId);
        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(booking));

        BookingResult bookingResult = bookingService.retrieveBooking(bookingId);

        assertEquals(booking, bookingResult);
    }

    @Test
    void testBookingDoesNotExist() {
        when(bookingRepository.findByBookingId(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookingService.retrieveBooking(UUID.randomUUID()));
    }

    private void assertEquals(Booking booking, BookingResult bookingResult) {
        assertThat(bookingResult.description()).isEqualTo(booking.getDescription());
        assertThat(bookingResult.email()).isEqualTo(booking.getEmail().getEmailAddress());
        assertThat(bookingResult.currency()).isEqualTo(booking.getCurrency());
        assertThat(bookingResult.price()).isEqualTo(booking.getPrice());
        assertThat(bookingResult.departmentName()).isEqualTo(booking.getDepartment().getName());
        assertThat(bookingResult.subscriptionStartDate()).isEqualTo(booking.getSubscriptionStartDate().toString());
    }

    private Booking createBookingDTO(UUID bookingId) {
        EuropeDepartment europeDepartment = new EuropeDepartment(UUID.randomUUID(), "EUROPE REPORTS");
        return new Booking(bookingId, "reports", BigDecimal.valueOf(25.00), Currency.USD, LocalDate.now(), new Email("muesterman@gmail.com"), europeDepartment);
    }
}