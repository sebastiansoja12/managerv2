package com.warehouse.deliverynetwork.infrastructure.adapter.primary.api;

import java.util.List;

public record DeliveryNetworkApiResponse(List<DepartmentConnectionApi> connections) {
}
