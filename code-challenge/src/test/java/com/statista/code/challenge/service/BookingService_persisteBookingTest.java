package com.statista.code.challenge.service;


import com.statista.code.challenge.controller.BookingDTO;
import com.statista.code.challenge.domainobjects.Booking;
import com.statista.code.challenge.domainobjects.Currency;
import com.statista.code.challenge.domainobjects.Email;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.EuropeDepartment;
import com.statista.code.challenge.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingService_persisteBookingTest {

    @InjectMocks
    private BookingServiceDefaultImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void testHappyPath() {
        BookingDTO bookingDTO = createBookingDTO();
        EuropeDepartment europeDepartment = new EuropeDepartment(UUID.randomUUID(), "EUROPE REPORTS");
        Booking booking = parseBooking(bookingDTO, europeDepartment);
        when(bookingRepository.createBooking(any())).thenReturn(booking);

        BookingResult bookingResult = bookingService.persistBooking(bookingDTO, UUID.randomUUID(), europeDepartment);

        assertEquals(booking, bookingResult);
    }

    private void assertEquals(Booking booking, BookingResult bookingResult) {
        assertThat(bookingResult.description()).isEqualTo(booking.getDescription());
        assertThat(bookingResult.email()).isEqualTo(booking.getEmail().getEmailAddress());
        assertThat(bookingResult.currency()).isEqualTo(booking.getCurrency());
        assertThat(bookingResult.price()).isEqualTo(booking.getPrice());
        assertThat(bookingResult.departmentName()).isEqualTo(booking.getDepartment().getName());
        assertThat(bookingResult.subscriptionStartDate()).isEqualTo(booking.getSubscriptionStartDate().toString());
    }

    private Booking parseBooking(BookingDTO bookingDTO, Department department) {
        BigDecimal price = bookingDTO.price().setScale(2, RoundingMode.UP);
        return Booking.builder().bookingId(UUID.randomUUID()).description(bookingDTO.description()).price(price).currency(bookingDTO.currency()).subscriptionStartDate(bookingDTO.subscriptionStartDate()).email(new Email(bookingDTO.email())).department(department).build();
    }

    private BookingDTO createBookingDTO() {
        return new BookingDTO("reports", BigDecimal.valueOf(25.00), Currency.USD, LocalDate.now(), "muesterman@gmail.com", "EUROPE REPORTS");
    }
}