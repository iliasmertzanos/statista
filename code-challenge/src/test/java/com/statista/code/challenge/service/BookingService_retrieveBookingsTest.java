package com.statista.code.challenge.service;


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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingService_retrieveBookingsTest {

    @InjectMocks
    private BookingServiceDefaultImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void testHappyPath() {
        UUID departmentId = UUID.randomUUID();
        EuropeDepartment europeDepartment = new EuropeDepartment(departmentId, "EUROPE REPORTS");
        Booking booking1 = createBookingDTO(europeDepartment);
        Booking booking2 = createBookingDTO(europeDepartment);
        when(bookingRepository.findBookingByDepartmentId(departmentId)).thenReturn(List.of(booking1, booking2));

        List<BookingResult> bookingResults = bookingService.retrieveBookings(departmentId);

        assertThat(bookingResults).hasSize(2).containsExactly(toBookingResult(booking1), toBookingResult(booking2));
    }

    @Test
    void testNoBookingWithThisDepartmentExists() {
        UUID departmentId = UUID.randomUUID();
        when(bookingRepository.findBookingByDepartmentId(departmentId)).thenReturn(Collections.emptyList());

        List<BookingResult> bookingResults = bookingService.retrieveBookings(departmentId);
        assertThat(bookingResults).isEmpty();
    }

    private Booking createBookingDTO(Department department) {
        return new Booking(UUID.randomUUID(), "reports", BigDecimal.valueOf(25.00), Currency.USD, LocalDate.now(), new Email("muesterman@gmail.com"), department);
    }

    private BookingResult toBookingResult(Booking booking) {
        return new BookingResult(booking.getDescription(), booking.getPrice(), booking.getCurrency(), booking.getSubscriptionStartDate(), booking.getEmail().getEmailAddress(), booking.getDepartment().getName());
    }
}