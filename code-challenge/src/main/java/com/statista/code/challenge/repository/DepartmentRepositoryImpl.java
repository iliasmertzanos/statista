package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.DepartmentRegion;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {
    @Override
    public Department findByProduct(DepartmentRegion departmentRegion) {
        return null;
    }
}
