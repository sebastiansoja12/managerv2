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
    public Department viewDepotByCode(DepartmentCode departmentCode) {
        return departmentRepository.findByCode(departmentCode);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public void addMultipleDepots(List<Department> departments) {
        departmentRepository.saveAll(departments);
    }

    @Override
    public void updateStreet(UpdateStreetRequest request) {
        final Department department = departmentRepository.findByCode(request.departmentCode());
        department.updateStreet(request.street());
        departmentRepository.save(department);
    }
}
