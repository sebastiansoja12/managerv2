package com.warehouse.pickuppoint.domain.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public final class PickupPointDepartmentPolicy {

    private PickupPointDepartmentPolicy() {
    }

    public static void ensureActive(final PickupPointDepartment department) {
        if (!department.active()) {
            throw new InvalidPickupPointConfigurationException("Pickup point department must be active");
        }
    }

    public static void ensureAssignable(final PickupPointDepartment department, final PickupPointStatus status) {
        if (status == PickupPointStatus.ACTIVE) {
            ensureActive(department);
        }
    }

    public static Set<DepartmentId> activeDepartmentIds(final Collection<PickupPointDepartment> departments) {
        return departments.stream()
                .filter(PickupPointDepartment::active)
                .map(PickupPointDepartment::departmentId)
                .collect(Collectors.toSet());
    }
}
