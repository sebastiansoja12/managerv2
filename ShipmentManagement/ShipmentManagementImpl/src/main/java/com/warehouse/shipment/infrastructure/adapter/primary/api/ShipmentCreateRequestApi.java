package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.PickupPointId;

public record ShipmentCreateRequestApi(PersonApi sender, PersonApi recipient, DimensionsApi dimensions,
                                       WeightApi weight, String contentDescription, MoneyApi declaredValue,
                                       String customerReference, MoneyApi price,
                                       ShipmentPriorityDto shipmentPriority,
                                       PackagingTypeDto packagingType, ShipmentServiceLevelDto serviceLevel,
                                       PickupMethodDto pickupMethod, DeliveryMethodDto deliveryMethod,
                                       PickupPointId pickupPointId, PickupPointId deliveryPickupPointId) {
}
