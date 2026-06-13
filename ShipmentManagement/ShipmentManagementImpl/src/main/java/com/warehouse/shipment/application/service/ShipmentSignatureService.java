package com.warehouse.shipment.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.warehouse.shipment.domain.context.ShipmentEventContext;
import com.warehouse.shipment.domain.event.ShipmentUpdated;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;

@Service
public class ShipmentSignatureService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentSignatureService(final ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public void updateSignature(final Signature signature) {
        final Shipment shipment = this.shipmentRepository.findById(signature.getShipmentId());
        shipment.changeSignature(signature);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentUpdated(shipment.snapshot(), Instant.now()));
    }
}
