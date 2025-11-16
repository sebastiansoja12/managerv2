package com.warehouse.department.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.TaxId;
import org.springframework.stereotype.Service;

@Service("department.departmentService")
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

    @Override
    public void changeTaxId(final DepartmentCode departmentCode, final TaxId newTaxId) {
        final Department department = this.departmentRepository.findByCode(departmentCode);
        department.changeTaxId(newTaxId);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void activateDepartment(final DepartmentCode departmentCode, final UserId modifiedBy) {
        final Department department = this.departmentRepository.findByCode(departmentCode);
        department.activate(modifiedBy);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void deactivateDepartment(final DepartmentCode departmentCode, final UserId modifiedBy) {
        final Department department = this.departmentRepository.findByCode(departmentCode);
        department.deactivate(modifiedBy);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void changeDepartmentType(final DepartmentCode departmentCode, final DepartmentType departmentType) {
        final Department department = this.departmentRepository.findByCode(departmentCode);
        department.changeDepartmentType(departmentType);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void changeAdminUser(final DepartmentCode departmentCode, final UserId userId) {
        final Department department = this.departmentRepository.findByCode(departmentCode);
        department.changeAdminUserId(userId);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void changeStatus(final DepartmentCode departmentCode, final Department.Status status) {
        final Department department = this.departmentRepository.findByCode(departmentCode);
        switch (status) {
            case ARCHIVED -> department.markAsArchived();
            case SUSPENDED -> department.markAsSuspended();
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        }
        this.departmentRepository.createOrUpdate(department);
    }
}
