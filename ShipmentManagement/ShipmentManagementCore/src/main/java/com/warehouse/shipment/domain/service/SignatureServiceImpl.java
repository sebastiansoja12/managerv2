package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.port.secondary.SignatureRepository;

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
    }
}
