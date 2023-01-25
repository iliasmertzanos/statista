package com.statista.code.challenge.service;

import com.statista.code.challenge.domainobjects.department.Department;

public interface DepartmentService {
    Department retrieveDepartment(String product);
}
