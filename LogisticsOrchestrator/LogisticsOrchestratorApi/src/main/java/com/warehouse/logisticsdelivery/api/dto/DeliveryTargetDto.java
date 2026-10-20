package com.warehouse.logisticsdelivery.api.dto;

public record DeliveryTargetDto(TargetType type, String id) {

    public enum TargetType {
        SHIPMENT,
        PALLET
    }
}
