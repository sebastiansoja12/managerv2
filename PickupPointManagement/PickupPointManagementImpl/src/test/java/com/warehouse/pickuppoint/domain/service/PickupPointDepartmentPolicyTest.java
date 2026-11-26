package com.warehouse.pickuppoint.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PickupPointDepartmentPolicyTest {

    private static final PickupPointDepartment ACTIVE_DEPARTMENT =
            new PickupPointDepartment(new DepartmentId(10L), new DepartmentCode("WAW"), true);
    private static final PickupPointDepartment INACTIVE_DEPARTMENT =
            new PickupPointDepartment(new DepartmentId(20L), new DepartmentCode("KRK"), false);

    @Test
    void shouldRejectInactiveDepartment() {
        assertThrows(
                InvalidPickupPointConfigurationException.class,
                () -> PickupPointDepartmentPolicy.ensureActive(INACTIVE_DEPARTMENT));
    }

    @Test
    void shouldRejectInactiveDepartmentForActivePickupPoint() {
        assertThrows(
                InvalidPickupPointConfigurationException.class,
                () -> PickupPointDepartmentPolicy.ensureAssignable(INACTIVE_DEPARTMENT, PickupPointStatus.ACTIVE));
    }

    @Test
    void shouldAllowInactiveDepartmentForSuspendedPickupPoint() {
        assertDoesNotThrow(
                () -> PickupPointDepartmentPolicy.ensureAssignable(INACTIVE_DEPARTMENT, PickupPointStatus.SUSPENDED));
    }

    @Test
    void shouldReturnOnlyActiveDepartmentIds() {
        final Set<DepartmentId> departmentIds =
                PickupPointDepartmentPolicy.activeDepartmentIds(List.of(ACTIVE_DEPARTMENT, INACTIVE_DEPARTMENT));

        assertEquals(Set.of(ACTIVE_DEPARTMENT.departmentId()), departmentIds);
    }
}
