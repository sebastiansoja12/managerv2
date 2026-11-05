package com.warehouse.deliverynetwork.api.dto;

import java.util.List;

public record ShipmentDeliveryRouteDto(List<RouteDepartmentDto> departments, Double distanceInKilometers) {

    public ShipmentDeliveryRouteDto {
        departments = departments == null ? List.of() : List.copyOf(departments);
    }
}
