package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.enumeration.PackagingType;

public record ShipmentRepackaging(PackagingType packagingType, Weight weight, Dimensions dimensions) {
}
