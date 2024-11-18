package com.warehouse.department.domain.port.primary;

import java.util.List;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.UpdateStreetRequest;

public class DepartmentPortImpl implements DepartmentPort {

    private final DepartmentRepository departmentRepository;

    public DepartmentPortImpl(final DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        return this.departmentRepository.findByCode(departmentCode);
    }

    @Override
    public List<Department> findAll() {
        return this.departmentRepository.findAll();
    }

    @Override
    public void addDepartments(final List<Department> departments) {
        this.departmentRepository.saveAll(departments);
    }

    @Override
    public void updateStreet(final UpdateStreetRequest request) {
        final Department department = this.departmentRepository.findByCode(request.departmentCode());
        department.updateStreet(request.street());
        this.departmentRepository.save(department);
    }
}
