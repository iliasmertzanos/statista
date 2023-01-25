package com.statista.code.challenge.controller;

import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.service.BookingResult;
import com.statista.code.challenge.service.BookingService;
import com.statista.code.challenge.service.DepartmentService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/booking/{transactionId}")
    public ResponseEntity updateBooking() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity getBookingById() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/booking/type/{type}")
    public ResponseEntity getBookingsOfType() {
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeExceptionHandler(RuntimeException e) {
        JSONObject response = new JSONObject();
        response.put("stackTrace", e.getStackTrace());
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
    }
}