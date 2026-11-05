package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeliveryRouteTest {

    @Test
    void shouldSumDistanceOfAllLegs() {
        final DeliveryRoute route = new DeliveryRoute(List.of(
                department(1L, "KT1", new DepartmentLocation(50.26, 19.02)),
                department(2L, "NCS", new DepartmentLocation(50.26, 19.02)),
                department(3L, "WA1", new DepartmentLocation(52.23, 21.01))));

        final double distance = route.distanceInKilometers().orElseThrow();

        assertTrue(distance > 250 && distance < 280);
    }

    @Test
    void shouldHaveNoDistanceWhenAnyDepartmentHasNoLocation() {
        final DeliveryRoute route = new DeliveryRoute(List.of(
                department(1L, "KT1", new DepartmentLocation(50.26, 19.02)),
                department(2L, "WA1", null)));

        assertTrue(route.distanceInKilometers().isEmpty());
    }

    @Test
    void shouldHaveZeroDistanceForSingleDepartmentRoute() {
        final DeliveryRoute route = new DeliveryRoute(List.of(
                department(1L, "KT1", new DepartmentLocation(50.26, 19.02))));

        assertEquals(0.0, route.distanceInKilometers().orElseThrow());
    }

    private DepartmentNode department(final Long departmentId, final String departmentCode,
                                      final DepartmentLocation location) {
        return new DepartmentNode(new DepartmentId(departmentId), new DepartmentCode(departmentCode),
                DepartmentType.BRANCH, DepartmentStatus.ACTIVE, location);
    }
}
