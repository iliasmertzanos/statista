package com.statista.code.challenge.service;


import com.statista.code.challenge.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingService_retrieveCurrentUsedCurrenciesTest {

    @InjectMocks
    private BookingServiceDefaultImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void testHappyPath() {
        when(bookingRepository.findAllUsedCurrencies()).thenReturn(List.of("USD", "EURO", "YUAN"));

        List<String> currencies = bookingService.retrieveCurrentUsedCurrencies();

        assertThat(currencies).hasSize(3).containsExactly("USD", "EURO", "YUAN");
    }

    @Test
    void testNoBookingWithThisDepartmentExists() {
        UUID departmentId = UUID.randomUUID();
        when(bookingRepository.findBookingByDepartmentId(departmentId)).thenReturn(Collections.emptyList());

        List<BookingResult> bookingResults = bookingService.retrieveBookings(departmentId);
        assertThat(bookingResults).isEmpty();
    }
}