package com.warehouse.deliverynetwork.domain.service;

import com.warehouse.deliverynetwork.domain.enumeration.HandlingOperationType;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.domain.vo.HandlingOperation;
import com.warehouse.deliverynetwork.domain.vo.RouteLeg;
import com.warehouse.deliverynetwork.domain.vo.ShipmentHandlingPlan;

import java.util.ArrayList;
import java.util.List;

public class ShipmentHandlingPlanFactory {

    public ShipmentHandlingPlan create(final List<DepartmentNode> routeDepartments) {
        final List<RouteLeg> legs = new ArrayList<>();
        for (int index = 1; index < routeDepartments.size(); index++) {
            final DepartmentNode originDepartment = routeDepartments.get(index - 1);
            final DepartmentNode destinationDepartment = routeDepartments.get(index);
            legs.add(new RouteLeg(index, originDepartment, destinationDepartment,
                    distanceInKilometers(originDepartment, destinationDepartment)));
        }

        final List<HandlingOperation> operations = new ArrayList<>();
        for (int index = 0; index < routeDepartments.size(); index++) {
            final DepartmentNode department = routeDepartments.get(index);
            final DepartmentNode previousDepartment = index > 0 ? routeDepartments.get(index - 1) : null;
            final DepartmentNode nextDepartment = index < routeDepartments.size() - 1
                    ? routeDepartments.get(index + 1)
                    : null;

            if (previousDepartment != null) {
                add(operations, department, HandlingOperationType.UNLOADING, previousDepartment);
            }
            if (previousDepartment != null && nextDepartment != null) {
                add(operations, department, department.isSortingFacility()
                        ? HandlingOperationType.SORTING
                        : HandlingOperationType.TRANSFER, null);
            }
            if (nextDepartment != null) {
                add(operations, department, HandlingOperationType.LOADING, nextDepartment);
            } else {
                add(operations, department, HandlingOperationType.HANDOVER_FOR_DELIVERY, null);
            }
        }

        return new ShipmentHandlingPlan(legs, operations);
    }

    private void add(final List<HandlingOperation> operations,
                     final DepartmentNode department,
                     final HandlingOperationType operationType,
                     final DepartmentNode counterpartDepartment) {
        operations.add(new HandlingOperation(operations.size() + 1, department, operationType, counterpartDepartment));
    }

    private Double distanceInKilometers(final DepartmentNode originDepartment,
                                        final DepartmentNode destinationDepartment) {
        if (originDepartment.location() == null || destinationDepartment.location() == null) {
            return null;
        }
        return originDepartment.distanceInKilometersTo(destinationDepartment);
    }
}
