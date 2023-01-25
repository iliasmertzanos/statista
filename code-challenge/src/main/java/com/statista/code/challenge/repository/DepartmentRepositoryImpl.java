package com.statista.code.challenge.repository;

import com.statista.code.challenge.domainobjects.department.AsiaDepartment;
import com.statista.code.challenge.domainobjects.department.Department;
import com.statista.code.challenge.domainobjects.department.EuropeDepartment;
import com.statista.code.challenge.domainobjects.department.UnitedStatesDepartment;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {
    private final Set<Department> departments = initializeDepartments();

    @Override
    public Optional<Department> findByDepartmentName(String departmentName) {
        return departments.stream().filter(department -> department.getName().equals(departmentName)).findFirst();
    }

    private Set<Department> initializeDepartments() {
        Set<Department> newDepartments = new HashSet<>();
        newDepartments.add(new AsiaDepartment(UUID.randomUUID(), "ASIA REPORTS"));
        newDepartments.add(new EuropeDepartment(UUID.randomUUID(), "EUROPE REPORTS"));
        newDepartments.add(new UnitedStatesDepartment(UUID.randomUUID(), "USA REPORTS"));
        newDepartments.add(new AsiaDepartment(UUID.randomUUID(), "ASIA MARKET INSIGHTS"));
        newDepartments.add(new EuropeDepartment(UUID.randomUUID(), "EUROPE MARKET INSIGHTS"));
        newDepartments.add(new UnitedStatesDepartment(UUID.randomUUID(), "USA MARKET INSIGHTS"));
        newDepartments.add(new AsiaDepartment(UUID.randomUUID(), "ASIA STATISTICS"));
        newDepartments.add(new EuropeDepartment(UUID.randomUUID(), "EUROPE STATISTICS"));
        newDepartments.add(new UnitedStatesDepartment(UUID.randomUUID(), "USA STATISTICS"));
        return newDepartments;
    }
}
