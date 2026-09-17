package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

import java.util.Optional;

public record ShipmentDepartments(
        ShipmentId shipmentId,
        TrackingNumber trackingNumber,
        DepartmentId originDepartmentId,
        DepartmentId destinationDepartmentId) {

    public Optional<DepartmentId> originDepartment() {
        return Optional.ofNullable(this.originDepartmentId);
    }
}
