package com.statista.code.challenge.domainobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Department {
    private Long departmentId;
    private List<Booking> bookings;
}