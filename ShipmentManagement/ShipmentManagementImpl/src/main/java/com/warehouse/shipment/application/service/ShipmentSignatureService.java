package com.warehouse.shipment.application.service;

import java.time.Instant;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import org.springframework.stereotype.Service;

import com.warehouse.shipment.domain.event.ShipmentUpdated;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;

@Service
public class ShipmentSignatureService {

    private final ShipmentRepository shipmentRepository;
    private final DomainEventPublisher domainEventPublisher;

    public ShipmentSignatureService(final ShipmentRepository shipmentRepository,
                                    final DomainEventPublisher domainEventPublisher) {
        this.shipmentRepository = shipmentRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public void updateSignature(final Signature signature) {
        final Shipment shipment = this.shipmentRepository.findById(signature.getShipmentId());
        shipment.changeSignature(signature);
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentUpdated(shipment.snapshot(), Instant.now()));
    }
}
