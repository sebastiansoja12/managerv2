package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DestinationDepartmentResolverPort;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.domain.vo.RecipientAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DeliveryDestinationResolver {

    private static final Logger log = LoggerFactory.getLogger(DeliveryDestinationResolver.class);

    private final ShipmentServicePort shipmentServicePort;

    private final DestinationDepartmentResolverPort destinationDepartmentResolverPort;

    private final DestinationCapacityAllocator destinationCapacityAllocator;

    public DeliveryDestinationResolver(final ShipmentServicePort shipmentServicePort,
                                       final DestinationDepartmentResolverPort destinationDepartmentResolverPort,
                                       final DestinationCapacityAllocator destinationCapacityAllocator) {
        this.shipmentServicePort = shipmentServicePort;
        this.destinationDepartmentResolverPort = destinationDepartmentResolverPort;
        this.destinationCapacityAllocator = destinationCapacityAllocator;
    }

    public Optional<DepartmentId> resolve(final RecipientAddress recipientAddress) {
        return this.destinationDepartmentResolverPort.resolve(recipientAddress)
                .map(this.destinationCapacityAllocator::allocate);
    }

    public Optional<DepartmentId> resolveForShipment(final ShipmentId shipmentId) {
        if (shipmentId == null) {
            return Optional.empty();
        }
        final Optional<DepartmentId> destinationDepartmentId = this.shipmentServicePort.findRecipientAddress(shipmentId)
                .flatMap(this::resolve);
        if (destinationDepartmentId.isEmpty()) {
            log.warn("Destination department of shipment {} could not be resolved", shipmentId.getValue());
        }
        return destinationDepartmentId;
    }
}
