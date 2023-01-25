package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.DepartmentRegion;

public interface DepartmentRepository {
    Department findByProduct(DepartmentRegion departmentRegion);
}
