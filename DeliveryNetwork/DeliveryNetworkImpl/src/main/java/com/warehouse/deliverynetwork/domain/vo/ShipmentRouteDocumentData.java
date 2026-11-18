package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public record ShipmentRouteDocumentData(
        ShipmentId shipmentId,
        TrackingNumber trackingNumber,
        List<DepartmentNode> departments,
        Map<DepartmentId, DepartmentAddress> addresses,
        ShipmentHandlingPlan handlingPlan,
        Double distanceInKilometers,
        Instant generatedAt,
        Locale locale) {

    public ShipmentRouteDocumentData {
        departments = List.copyOf(departments);
        addresses = Map.copyOf(addresses);
    }

    public Optional<DepartmentAddress> address(final DepartmentNode department) {
        return Optional.ofNullable(this.addresses.get(department.departmentId()));
    }
}
