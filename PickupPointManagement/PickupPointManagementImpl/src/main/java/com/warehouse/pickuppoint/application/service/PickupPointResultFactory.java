package com.warehouse.pickuppoint.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointResult;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;

import java.time.Instant;
import java.util.Map;

public class PickupPointResultFactory {

    private final PickupPointDepartmentService pickupPointDepartmentService;

    public PickupPointResultFactory(final PickupPointDepartmentService pickupPointDepartmentService) {
        this.pickupPointDepartmentService = pickupPointDepartmentService;
    }

    public PickupPointResult create(final PickupPoint pickupPoint, final Instant timestamp) {
        return create(pickupPoint, this.pickupPointDepartmentService.getCurrentOperatorDepartments(), timestamp);
    }

    public PickupPointResult create(
            final PickupPoint pickupPoint,
            final Map<DepartmentId, PickupPointDepartment> departments,
            final Instant timestamp) {
        final DepartmentId departmentId = pickupPoint.snapshot().departmentId();
        final PickupPointDepartment department = departmentId == null ? null : departments.get(departmentId);
        return create(pickupPoint, department, timestamp);
    }

    public PickupPointResult create(
            final PickupPoint pickupPoint,
            final PickupPointDepartment department,
            final Instant timestamp) {
        final Boolean openNow = pickupPoint.snapshot().openingSchedule() == null
                ? null
                : pickupPoint.isOpenAt(timestamp);
        return new PickupPointResult(pickupPoint.snapshot(), department, openNow, timestamp);
    }
}
