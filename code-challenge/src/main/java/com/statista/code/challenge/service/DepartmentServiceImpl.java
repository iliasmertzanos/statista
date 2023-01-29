package com.statista.code.challenge.service;

import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department retrieveDepartment(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName).orElseThrow(() -> new NoSuchElementException("The department " + departmentName + " does not exist"));
    }
}