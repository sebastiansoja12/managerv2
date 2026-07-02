package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;

import java.util.Objects;

public record PickupPointSelectionCriteria(
        PickupPointCapability requiredCapability,
        PickupPointType requiredType,
        CountryCode countryCode,
        PickupPointShipmentSize shipmentSize,
        boolean dangerousGoods) {

    public PickupPointSelectionCriteria {
        Objects.requireNonNull(requiredCapability, "Required capability cannot be null");
        Objects.requireNonNull(requiredType, "Required pickup point type cannot be null");
        Objects.requireNonNull(countryCode, "Country code cannot be null");
        Objects.requireNonNull(shipmentSize, "Shipment size cannot be null");
    }
}
