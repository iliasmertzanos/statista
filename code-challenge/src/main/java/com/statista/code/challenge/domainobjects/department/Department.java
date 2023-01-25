package com.statista.code.challenge.domainobjects.department;

import com.statista.code.challenge.domainobjects.Booking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Department {
    private Long departmentId;
    private String name;
    private List<Booking> bookings;

    // In the code challenge requirements named as doBusiness,
    // but here named according to responsibility of this method
    public abstract String getBookingPriceToLocalCurrency(Long booking);
}