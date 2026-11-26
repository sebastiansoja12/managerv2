package com.warehouse.logisticsdelivery.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShipmentStatusDeliveryStrategyResolver {

    private final Map<ShipmentStatus, ShipmentStatusDeliveryStrategy> strategies;

    public ShipmentStatusDeliveryStrategyResolver(final List<ShipmentStatusDeliveryStrategy> strategies) {
        this.strategies = new EnumMap<>(ShipmentStatus.class);
        strategies.forEach(this::register);
    }

    public Optional<ShipmentStatusDeliveryStrategy> resolve(final ShipmentStatus shipmentStatus) {
        return Optional.ofNullable(strategies.get(shipmentStatus));
    }

    private void register(final ShipmentStatusDeliveryStrategy strategy) {
        strategy.supportedStatuses().forEach(status -> {
            final ShipmentStatusDeliveryStrategy previous = strategies.putIfAbsent(status, strategy);
            if (previous != null) {
                throw new IllegalArgumentException("Multiple shipment status delivery strategies for status: "
                        + status);
            }
        });
    }
}
