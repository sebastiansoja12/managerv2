package com.warehouse.shipment.domain.vo.conf;

public record ShipmentLimits(
        double maxWeight,
        double minWeight,
        double maxLength,
        double maxWidth,
        double maxHeight,
        double maxShipmentValue,
        boolean allowOversized
) {

    public static ShipmentLimits defaults() {
        return new ShipmentLimits(31.5, 0.0, 120.0, 80.0, 80.0, 0.0, false);
    }
}
