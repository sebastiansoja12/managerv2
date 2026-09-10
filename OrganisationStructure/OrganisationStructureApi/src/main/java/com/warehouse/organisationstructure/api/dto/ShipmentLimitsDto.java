package com.warehouse.organisationstructure.api.dto;

public record ShipmentLimitsDto(
        double maxWeight,
        double minWeight,
        double maxLength,
        double maxWidth,
        double maxHeight,
        double maxShipmentValue,
        boolean allowOversized
) {
}
