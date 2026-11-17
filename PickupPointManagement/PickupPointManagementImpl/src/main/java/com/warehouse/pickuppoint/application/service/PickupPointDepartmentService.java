package com.warehouse.pickuppoint.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.application.exception.PickupPointDepartmentNotFoundException;
import com.warehouse.pickuppoint.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import com.warehouse.pickuppoint.domain.service.PickupPointDepartmentPolicy;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PickupPointDepartmentService {

    private final DepartmentDirectoryServicePort departmentDirectoryServicePort;

    public PickupPointDepartmentService(final DepartmentDirectoryServicePort departmentDirectoryServicePort) {
        this.departmentDirectoryServicePort = departmentDirectoryServicePort;
    }

    public PickupPointDepartment getRequired(final DepartmentId departmentId) {
        if (departmentId == null) {
            throw new InvalidPickupPointConfigurationException("Pickup point department is required");
        }
        return this.departmentDirectoryServicePort.findById(departmentId)
                .orElseThrow(() -> new PickupPointDepartmentNotFoundException(departmentId));
    }

    public Map<DepartmentId, PickupPointDepartment> getCurrentOperatorDepartments() {
        return this.departmentDirectoryServicePort.getCurrentOperatorDepartments().stream()
                .collect(Collectors.toMap(PickupPointDepartment::departmentId, Function.identity()));
    }

    public void ensureActive(final DepartmentId departmentId) {
        PickupPointDepartmentPolicy.ensureActive(getRequired(departmentId));
    }

    public void ensureAssignable(final DepartmentId departmentId, final PickupPointStatus status) {
        Optional.ofNullable(departmentId)
                .map(this::getRequired)
                .ifPresent(department -> PickupPointDepartmentPolicy.ensureAssignable(department, status));
    }
}
