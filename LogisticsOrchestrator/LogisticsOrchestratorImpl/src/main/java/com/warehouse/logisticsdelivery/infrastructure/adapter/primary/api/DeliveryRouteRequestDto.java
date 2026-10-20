package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

public record DeliveryRouteRequestDto(Long originDepartmentId,
                                      Long destinationDepartmentId,
                                      String pickupMethod,
                                      String pickupPointId,
                                      String deliveryPickupPointId) {
}
