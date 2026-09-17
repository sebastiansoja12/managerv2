package com.warehouse.deliverynetwork.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.enumeration.HandlingOperationType;
import com.warehouse.deliverynetwork.domain.vo.DepartmentLocation;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.domain.vo.HandlingOperation;
import com.warehouse.deliverynetwork.domain.vo.ShipmentHandlingPlan;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipmentHandlingPlanFactoryTest {

    private final ShipmentHandlingPlanFactory shipmentHandlingPlanFactory = new ShipmentHandlingPlanFactory();

    @Test
    void shouldPlanLoadingSortingTransferAndHandover() {
        final DepartmentNode origin = department(1L, "KT1", DepartmentType.BRANCH, new DepartmentLocation(50.29, 18.67));
        final DepartmentNode sortingFacility = department(2L, "NCS", DepartmentType.SORTING_FACILITY,
                new DepartmentLocation(51.76, 19.46));
        final DepartmentNode branchTransfer = department(3L, "GD1", DepartmentType.BRANCH, null);
        final DepartmentNode destination = department(4L, "GDP", DepartmentType.BRANCH, new DepartmentLocation(54.40, 18.66));

        final ShipmentHandlingPlan plan = this.shipmentHandlingPlanFactory.create(
                List.of(origin, sortingFacility, branchTransfer, destination));

        assertEquals(List.of(
                HandlingOperationType.LOADING,
                HandlingOperationType.UNLOADING,
                HandlingOperationType.SORTING,
                HandlingOperationType.LOADING,
                HandlingOperationType.UNLOADING,
                HandlingOperationType.TRANSFER,
                HandlingOperationType.LOADING,
                HandlingOperationType.UNLOADING,
                HandlingOperationType.HANDOVER_FOR_DELIVERY), plan.operations().stream()
                .map(HandlingOperation::operationType)
                .toList());
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), plan.operations().stream()
                .map(HandlingOperation::sequence)
                .toList());
        assertEquals(sortingFacility, plan.operations().getFirst().counterpartDepartment());
        assertEquals(origin, plan.operations().get(1).counterpartDepartment());
        assertEquals(3, plan.legs().size());
        assertTrue(plan.legs().getFirst().distanceInKilometers() > 160);
        assertNull(plan.legs().get(1).distanceInKilometers());
    }

    @Test
    void shouldOnlyHandOverWhenOriginDeliversShipment() {
        final DepartmentNode origin = department(1L, "KT1", DepartmentType.BRANCH, null);

        final ShipmentHandlingPlan plan = this.shipmentHandlingPlanFactory.create(List.of(origin));

        assertTrue(plan.legs().isEmpty());
        assertEquals(List.of(HandlingOperationType.HANDOVER_FOR_DELIVERY), plan.operations().stream()
                .map(HandlingOperation::operationType)
                .toList());
    }

    private static DepartmentNode department(final Long departmentId,
                                             final String departmentCode,
                                             final DepartmentType departmentType,
                                             final DepartmentLocation location) {
        return new DepartmentNode(new DepartmentId(departmentId), new DepartmentCode(departmentCode), departmentType,
                DepartmentStatus.ACTIVE, location);
    }
}
