package com.warehouse.department.domain.service;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;

public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(final DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void createDepartment(final Department department) {
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        return departmentRepository.findByCode(departmentCode);
    }

    @Override
    public void changeAddress(final DepartmentCode departmentCode, final Address address) {
        final Department department = this.departmentRepository.findByCode(departmentCode);
        department.changeAddress(address);
        this.departmentRepository.createOrUpdate(department);
    }
}
