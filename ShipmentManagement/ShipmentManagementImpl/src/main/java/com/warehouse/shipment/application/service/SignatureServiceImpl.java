package com.warehouse.shipment.application.service;

import java.time.Instant;

import com.warehouse.shipment.domain.context.ShipmentEventContext;
import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.port.secondary.SignatureRepository;

public class SignatureServiceImpl implements SignatureService {

    private final SignatureRepository signatureRepository;

    private final ShipmentRepository shipmentRepository;

    public SignatureServiceImpl(final SignatureRepository signatureRepository,
                                final ShipmentRepository shipmentRepository) {
        this.signatureRepository = signatureRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void createSignature(final Signature signature) {
        this.signatureRepository.save(signature);
        ShipmentEventContext.eventPublisher().publishEvent(new SignatureSigned(signature.snapshot(), Instant.now()));
    }
}
