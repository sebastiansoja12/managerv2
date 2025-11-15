package com.warehouse.department.domain.port.primary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.exception.DepartmentAlreadyExistsException;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreate;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.port.secondary.TenantAdminProvisioningPort;
import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.validator.Validator;
import com.warehouse.department.domain.vo.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DepartmentPortImpl implements DepartmentPort {

    private final DepartmentRepository departmentRepository;

    private final DepartmentService departmentService;

    private final TenantAdminProvisioningPort tenantAdminProvisioningPort;

    private final Validator validator;

    public DepartmentPortImpl(final DepartmentRepository departmentRepository,
                              final DepartmentService departmentService,
                              final TenantAdminProvisioningPort tenantAdminProvisioningPort,
                              final Validator validator) {
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
        this.tenantAdminProvisioningPort = tenantAdminProvisioningPort;
        this.validator = validator;
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
    @Transactional
    public DepartmentCreateResponse createDepartments(final DepartmentCreateRequest request) {

        validateRequest(request.getDepartments());

        checkIfDepartmentWithGivenCodeAlreadyExists(request.getDepartments());

        final Map<Department, Boolean> createdDepartments = new HashMap<>();
        for (final DepartmentCreate departmentCreate : request.getDepartments()) {
            final DepartmentCode departmentCode = departmentCreate.getDepartmentCode();
			final Department department = new Department(departmentCode, departmentCreate.getCity(),
					departmentCreate.getStreet(), departmentCreate.getPostalCode(),
					departmentCreate.getNip(), departmentCreate.getTelephoneNumber(),
					departmentCreate.getOpeningHours(), departmentCreate.getEmail(), true,
					departmentCreate.getCountryCode(), departmentCreate.getDepartmentType());
            this.departmentService.createDepartment(department);

            createdDepartments.put(department, true);
		}

        createdDepartments.forEach((key, value) -> tenantAdminProvisioningPort.createInitialAdminUser(
                key.snapshot()
        ));

        return new DepartmentCreateResponse(createdDepartments);
    }

    private void validateRequest(final List<DepartmentCreate> departments) {
        departments.forEach(dep -> validator.validateType(dep.getDepartmentType()));
    }

    private void checkIfDepartmentWithGivenCodeAlreadyExists(final List<DepartmentCreate> deps) {
		deps.forEach(dep -> {
			final Department department = this.departmentService.findByDepartmentCode(dep.getDepartmentCode());
			if (department != null) {
				throw new DepartmentAlreadyExistsException(
						"Department with code " + dep.getDepartmentCode().getValue() + " already exists");
			}
		});
    }

    @Override
    public void changeAddress(final UpdateAddressRequest request) {
        validateAddress(request.address());

        this.departmentService.changeAddress(request.departmentCode(), request.address());
    }

    @Override
    public IdentificationNumberChangeResponse changeIdentificationNumber(final IdentificationNumberChangeRequest request) {
        final DepartmentCode departmentCode = request.departmentCode();
        final Department department = this.departmentService.findByDepartmentCode(departmentCode);
        final String oldIdentificationNumber = department.getNip();
        final String newIdentificationNumber = request.identificationNumber();
        final boolean identificationNumberChanged = !oldIdentificationNumber.equals(newIdentificationNumber);
        if (identificationNumberChanged) {
            this.departmentService.changeIdentificationNumber(departmentCode, newIdentificationNumber);
        }
        return new IdentificationNumberChangeResponse(departmentCode, oldIdentificationNumber, newIdentificationNumber);
    }

    @Override
    public void changeDepartmentActive(final DepartmentCode departmentCode, final Boolean active) {
        final UserId userId = (UserId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (active) {
            this.departmentService.activateDepartment(departmentCode, userId);
        } else {
            this.departmentService.deactivateDepartment(departmentCode, userId);
        }
    }

    @Override
    public void changeDepartmentType(final DepartmentCode departmentCode, final DepartmentType departmentType) {
        this.validator.validateType(departmentType);
        this.departmentService.changeDepartmentType(departmentCode, departmentType);
    }

    private void validateAddress(final Address address) {
        address.validate();
    }
}
