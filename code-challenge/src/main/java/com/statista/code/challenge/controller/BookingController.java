package com.statista.code.challenge.controller;

import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.PaymentProposal;
import com.statista.code.challenge.service.BookingResult;
import com.statista.code.challenge.service.BookingService;
import com.statista.code.challenge.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/bookingservice")
public class BookingController {

    private final BookingService bookingService;
    private final DepartmentService departmentService;

    public BookingController(BookingService bookingService, DepartmentService departmentService) {
        this.bookingService = bookingService;
        this.departmentService = departmentService;
    }

    @PostMapping("/booking")
    public ResponseEntity<BookingResult> createBooking(@RequestBody BookingDTO bookingDto) {
        Department department = departmentService.retrieveDepartment(bookingDto.departmentName());
        BookingResult bookingResult = bookingService.createBookingAndSendEmail(bookingDto, department);
        return ResponseEntity.ok(bookingResult);
    }

    @PutMapping("/booking/{bookingId}")
    public ResponseEntity<BookingResult> updateBooking(@PathVariable String bookingId, @RequestBody BookingDTO bookingDto) {
        Department department = departmentService.retrieveDepartment(bookingDto.departmentName());
        BookingResult bookingResult = bookingService.persistBooking(bookingDto, bookingId, department);
        return ResponseEntity.ok(bookingResult);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<BookingResult> getBookingById(@PathVariable String bookingId) {
        BookingResult bookingResult = bookingService.retrieveBooking(bookingId);
        return ResponseEntity.ok(bookingResult);
    }

    @GetMapping("/booking/department/{departmentId}")
    public ResponseEntity<List<BookingResult>> getBookingsByDepartments(@PathVariable String departmentId) {
        List<BookingResult> bookingResults = bookingService.retrieveBookings(departmentId);
        return ResponseEntity.ok(bookingResults);
    }

    @GetMapping("/booking/currencies")
    public ResponseEntity<List<String>> getCurrentUsedCurrencies() {
        List<String> bookingResults = bookingService.retrieveCurrentUsedCurrencies();
        return ResponseEntity.ok(bookingResults);
    }

    @GetMapping("/booking/sum/{currency}")
    public ResponseEntity<BigDecimal> getBookingsTotalPriceByCurrency(@PathVariable String currency) {
        BigDecimal bookingsTotalPrice = bookingService.retrieveBookingsTotalPriceByCurrency(currency);
        return ResponseEntity.ok(bookingsTotalPrice);
    }

    @GetMapping("/booking/paymentProposal/{bookingId}")
    public ResponseEntity<PaymentProposal> getBookingPaymentProposal(@PathVariable String bookingId) {
        PaymentProposal paymentProposal = bookingService.determineBookingPaymentProposal(bookingId);
        return ResponseEntity.ok(paymentProposal);
    }
}