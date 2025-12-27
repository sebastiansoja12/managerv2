package com.warehouse.department.domain.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.event.*;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.registry.DomainRegistry;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.TaxId;

@Service("department.departmentService")
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(final DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void createDepartment(final Department department) {
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(
                new DepartmentCreated(department.snapshot(), Instant.now())
        );
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        return departmentRepository.findByDepartmentCode(departmentCode);
    }

    @Override
    public void changeAddress(final DepartmentCode departmentCode, final Address address) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeAddress(address);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void changeTaxId(final DepartmentCode departmentCode, final TaxId newTaxId) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeTaxId(newTaxId);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void activateDepartment(final DepartmentCode departmentCode, final UserId modifiedBy) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.activate(modifiedBy);
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(new DepartmentActivated(department.snapshot(), Instant.now()));
    }

    @Override
    public void deactivateDepartment(final DepartmentCode departmentCode, final UserId modifiedBy) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.deactivate(modifiedBy);
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(new DepartmentDeactivated(department.snapshot(), Instant.now()));
    }

    @Override
    public void changeDepartmentType(final DepartmentCode departmentCode, final DepartmentType departmentType) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeDepartmentType(departmentType);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void changeAdminUser(final DepartmentCode departmentCode, final UserId userId) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeAdminUserId(userId);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void changeStatus(final DepartmentCode departmentCode, final Department.Status status) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        switch (status) {
            case ARCHIVED -> department.markAsArchived();
            case SUSPENDED -> department.markAsSuspended();
            case DELETED -> department.markAsDeleted();
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        }
        this.departmentRepository.createOrUpdate(department);
        final DepartmentEvent event = createDepartmentEvent(status, department);
        DomainRegistry.eventPublisher().publishEvent(event);
    }

    @Override
    public void changeEmail(final DepartmentCode departmentCode, final String email) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeEmail(email);
        this.departmentRepository.createOrUpdate(department);
    }

    private DepartmentEvent createDepartmentEvent(final Department.Status status, final Department department) {
        return switch (status) {
            case ACTIVE -> new DepartmentActivated(department.snapshot(), Instant.now());
            case INACTIVE -> new DepartmentDeactivated(department.snapshot(), Instant.now());
            case ARCHIVED -> new DepartmentArchived(department.snapshot(), Instant.now());
            case SUSPENDED -> new DepartmentSuspended(department.snapshot(), Instant.now());
            case DELETED -> new DepartmentDeleted(department.snapshot(), Instant.now());
        };
    }
}
