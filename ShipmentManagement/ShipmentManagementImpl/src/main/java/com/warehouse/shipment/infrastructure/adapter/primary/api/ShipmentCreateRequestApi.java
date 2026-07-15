package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.PickupPointId;

public record ShipmentCreateRequestApi(PersonApi sender, PersonApi recipient, ShipmentSizeDto shipmentSize,
									   MoneyApi price, DangerousGoodApi dangerousGood, ShipmentPriorityDto shipmentPriority,
                                       String issuerCountryCode, String receiverCountryCode,
                                       PickupMethodDto pickupMethod, DeliveryMethodDto deliveryMethod,
                                       PickupPointId pickupPointId, PickupPointId deliveryPickupPointId) {
}
