package com.warehouse.shipment.application.service;

import java.time.Instant;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.application.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.port.secondary.SignatureRepository;
import org.springframework.transaction.annotation.Transactional;

public class SignatureServiceImpl implements SignatureService {

    private final SignatureRepository signatureRepository;

    private final ShipmentReadModelRepository shipmentReadModelRepository;

    private final ShipmentRepository shipmentRepository;

    private final DomainEventPublisher domainEventPublisher;

    public SignatureServiceImpl(final SignatureRepository signatureRepository,
                                final ShipmentRepository shipmentRepository,
                                final ShipmentReadModelRepository shipmentReadModelRepository,
                                final DomainEventPublisher domainEventPublisher) {
        this.signatureRepository = signatureRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentReadModelRepository = shipmentReadModelRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    @Transactional
    public void createSignature(final Signature signature) {
        final Shipment shipment = this.shipmentRepository.findById(signature.getShipmentId());
        this.signatureRepository.save(signature);
        this.shipmentReadModelRepository.sync(shipment.snapshot());
        this.domainEventPublisher.publish(new SignatureSigned(signature.snapshot(), Instant.now()));
    }
}
