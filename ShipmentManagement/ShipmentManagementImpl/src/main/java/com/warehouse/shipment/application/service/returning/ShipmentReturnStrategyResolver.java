package com.warehouse.shipment.application.service.returning;

import com.warehouse.shipment.domain.enumeration.ReturnStatus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ShipmentReturnStrategyResolver {

    private final Map<ReturnStatus, ShipmentReturnStrategy> strategies;

    public ShipmentReturnStrategyResolver(final List<ShipmentReturnStrategy> strategies) {
        this.strategies = new EnumMap<>(ReturnStatus.class);
        strategies.forEach(this::register);
    }

    public ShipmentReturnStrategy resolve(final ReturnStatus returnStatus) {
        final ShipmentReturnStrategy strategy = strategies.get(returnStatus);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported return status: " + returnStatus);
        }
        return strategy;
    }

    private void register(final ShipmentReturnStrategy strategy) {
        strategy.supportedStatuses().forEach(status -> {
            final ShipmentReturnStrategy previous = strategies.putIfAbsent(status, strategy);
            if (previous != null) {
                throw new IllegalArgumentException("Multiple shipment return strategies for status: " + status);
            }
        });
    }
}
