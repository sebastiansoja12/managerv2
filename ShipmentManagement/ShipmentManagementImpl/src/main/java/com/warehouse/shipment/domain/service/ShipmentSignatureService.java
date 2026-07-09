package com.warehouse.shipment.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;

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
    }
}
