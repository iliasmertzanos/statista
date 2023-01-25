package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.department.Department;

import java.util.Optional;

public interface DepartmentRepository {
    Optional<Department> findByDepartmentName(String departmentName);
}
