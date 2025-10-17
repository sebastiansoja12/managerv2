package com.warehouse.department.domain.port.primary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.exception.ForbiddenDepartmentTypeException;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreate;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentCreateResponse;
import com.warehouse.department.domain.vo.UpdateStreetRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentPortImpl implements DepartmentPort {

    private final DepartmentRepository departmentRepository;

    private final DepartmentService departmentService;

    public DepartmentPortImpl(final DepartmentRepository departmentRepository,
                              final DepartmentService departmentService) {
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
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
        this.departmentRepository.createOrUpdateAll(departments);
    }

    @Override
    public DepartmentCreateResponse createDepartments(final DepartmentCreateRequest request) {

        validateRequest(request.getDepartments());

        checkIfDepartmentWithGivenCodeAlreadyExists(request.getDepartments());

        final Map<DepartmentCode, Boolean> createdDepartments = new HashMap<>();
        for (final DepartmentCreate departmentCreate : request.getDepartments()) {
            final DepartmentCode departmentCode = departmentCreate.getDepartmentCode();
			final Department department = new Department(departmentCode, departmentCreate.getCity(),
					departmentCreate.getStreet(), departmentCreate.getCountry(), departmentCreate.getPostalCode(),
					departmentCreate.getNip(), departmentCreate.getTelephoneNumber(),
					departmentCreate.getOpeningHours(), true,
					departmentCreate.getCountryCode(), departmentCreate.getDepartmentType());
            this.departmentService.createDepartment(department);

            createdDepartments.put(departmentCode, true);
		}

        return new DepartmentCreateResponse(createdDepartments);
    }

    private void validateRequest(final List<DepartmentCreate> departments) {
        departments.forEach(dep -> {
            if (dep.getDepartmentType() == DepartmentType.HEADQUARTERS) {
                log.error("Department type HEADQUARTERS is not allowed");
                throw new ForbiddenDepartmentTypeException("Forbidden department type: " + dep.getDepartmentType());
            }
        });
    }

    private void checkIfDepartmentWithGivenCodeAlreadyExists(final List<DepartmentCreate> deps) {
		deps.forEach(dep -> {
			final Department department = this.departmentService.findByDepartmentCode(dep.getDepartmentCode());
			if (department != null) {
				throw new IllegalArgumentException(
						"Department with code " + dep.getDepartmentCode().getValue() + " already exists");
			}
		});
    }

    @Override
    public void updateStreet(final UpdateStreetRequest request) {
        final Department department = this.departmentRepository.findByCode(request.departmentCode());
        department.updateStreet(request.street());
        this.departmentRepository.createOrUpdate(department);
    }
}
