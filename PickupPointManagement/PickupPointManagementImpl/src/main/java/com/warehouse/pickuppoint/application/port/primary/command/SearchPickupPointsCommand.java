package com.warehouse.pickuppoint.application.port.primary.command;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;

public record SearchPickupPointsCommand(
        String query,
        PickupPointType type,
        PickupPointStatus status,
        PickupPointCapability capability,
        DepartmentId departmentId,
        CountryCode countryCode,
        String city,
        String networkCode,
        Double west,
        Double south,
        Double east,
        Double north,
        PickupPointShipmentSize shipmentSize,
        Boolean dangerousGoods,
        int page,
        int size) {

    public SearchPickupPointsCommand {
        if (page < 0) {
            throw new IllegalArgumentException("Pickup point page cannot be negative");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Pickup point page size must be between 1 and 100");
        }
        final boolean anyBound = west != null || south != null || east != null || north != null;
        final boolean everyBound = west != null && south != null && east != null && north != null;
        if (anyBound && !everyBound) {
            throw new IllegalArgumentException("Pickup point bounding box must contain all four coordinates");
        }
        if (everyBound && (west < -180 || east > 180 || south < -90 || north > 90
                || west >= east || south >= north)) {
            throw new IllegalArgumentException("Pickup point bounding box is invalid");
        }
    }

    public boolean eligibilitySearch() {
        return this.shipmentSize != null || this.dangerousGoods != null;
    }
}
