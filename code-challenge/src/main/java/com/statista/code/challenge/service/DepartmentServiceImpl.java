package com.statista.code.challenge.service;

import com.statista.code.challenge.domainobjects.department.*;
import com.statista.code.challenge.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department retrieveDepartment(String departmentRegionValue) {
        DepartmentRegion departmentRegion = DepartmentRegion.valueOf(departmentRegionValue);
        return switch (departmentRegion) {
            case EUROPE -> (EuropeDepartment) departmentRepository.findByProduct(departmentRegion);
            case UNITED_STATES -> (UnitedStatesDepartment) departmentRepository.findByProduct(departmentRegion);
            case ASIA -> (AsiaDepartment) departmentRepository.findByProduct(departmentRegion);
        };
    }
}
