package com.warehouse.deliverynetwork.infrastructure.adapter.primary.api;

import java.util.List;

public record ShipmentDeliveryRouteApiResponse(
        String shipmentId,
        String trackingNumber,
        List<DeliveryRouteDepartmentApi> departments,
        Double distanceInKilometers) {
}
