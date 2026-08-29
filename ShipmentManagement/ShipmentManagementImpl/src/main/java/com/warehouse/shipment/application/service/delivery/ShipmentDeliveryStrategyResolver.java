package com.warehouse.shipment.application.service.delivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ShipmentDeliveryStrategyResolver {

    private final Map<DeliveryStatus, ShipmentDeliveryStrategy> strategies;

    public ShipmentDeliveryStrategyResolver(final List<ShipmentDeliveryStrategy> strategies) {
        this.strategies = new EnumMap<>(DeliveryStatus.class);
        strategies.forEach(this::register);
    }

    public ShipmentDeliveryStrategy resolve(final DeliveryStatus deliveryStatus) {
        final ShipmentDeliveryStrategy strategy = strategies.get(deliveryStatus);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported delivery status: " + deliveryStatus);
        }
        return strategy;
    }

    private void register(final ShipmentDeliveryStrategy strategy) {
        strategy.supportedStatuses().forEach(status -> {
            final ShipmentDeliveryStrategy previous = strategies.putIfAbsent(status, strategy);
            if (previous != null) {
                throw new IllegalArgumentException("Multiple delivery strategies for status: " + status);
            }
        });
    }
}
