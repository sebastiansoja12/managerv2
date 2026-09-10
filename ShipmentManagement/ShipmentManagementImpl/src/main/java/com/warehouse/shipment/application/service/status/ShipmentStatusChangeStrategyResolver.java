package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ShipmentStatusChangeStrategyResolver {

    private final Map<ShipmentStatus, ShipmentStatusChangeStrategy> strategies;

    public ShipmentStatusChangeStrategyResolver(final List<ShipmentStatusChangeStrategy> strategies) {
        this.strategies = new EnumMap<>(ShipmentStatus.class);
        strategies.forEach(this::register);
    }

    public ShipmentStatusChangeStrategy resolve(final ShipmentStatus shipmentStatus) {
        final ShipmentStatusChangeStrategy strategy = strategies.get(shipmentStatus);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported shipment status: " + shipmentStatus);
        }
        return strategy;
    }

    private void register(final ShipmentStatusChangeStrategy strategy) {
        strategy.supportedStatuses().forEach(status -> {
            final ShipmentStatusChangeStrategy previous = strategies.putIfAbsent(status, strategy);
            if (previous != null) {
                throw new IllegalArgumentException("Multiple shipment status strategies for status: " + status);
            }
        });
    }
}
