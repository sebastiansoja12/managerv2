package com.warehouse.shipment.application.service;

import java.time.Instant;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.port.secondary.SignatureRepository;

public class SignatureServiceImpl implements SignatureService {

    private final SignatureRepository signatureRepository;

    private final ShipmentRepository shipmentRepository;

    private final DomainEventPublisher domainEventPublisher;

    public SignatureServiceImpl(final SignatureRepository signatureRepository,
                                final ShipmentRepository shipmentRepository,
                                final DomainEventPublisher domainEventPublisher) {
        this.signatureRepository = signatureRepository;
        this.shipmentRepository = shipmentRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void createSignature(final Signature signature) {
        this.signatureRepository.save(signature);
        this.domainEventPublisher.publish(new SignatureSigned(signature.snapshot(), Instant.now()));
    }
}
