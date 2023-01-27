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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingService_retrieveBookingsTotalPriceByCurrencyTest {

    @InjectMocks
    private BookingServiceDefaultImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void testHappyPath() {
        Booking booking1 = createBookingDTO(50.00);
        Booking booking2 = createBookingDTO(80.00);
        when(bookingRepository.findBookingsByCurrency(Currency.EURO)).thenReturn(List.of(booking1, booking2));

        BigDecimal totalPrice = bookingService.retrieveBookingsTotalPriceByCurrency(Currency.EURO);

        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(130.00).setScale(2, RoundingMode.UP));
    }

    @Test
    void testNoBookingFound() {
        when(bookingRepository.findBookingsByCurrency(Currency.EURO)).thenReturn(Collections.emptyList());

        BigDecimal totalPrice = bookingService.retrieveBookingsTotalPriceByCurrency(Currency.EURO);

        assertThat(totalPrice).isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.UP));
    }

    private Booking createBookingDTO(double price) {
        EuropeDepartment europeDepartment = new EuropeDepartment(UUID.randomUUID(), "EUROPE REPORTS");
        return new Booking(UUID.randomUUID(), "reports", BigDecimal.valueOf(price), Currency.USD, LocalDate.now(), new Email("muesterman@gmail.com"), europeDepartment);
    }
}