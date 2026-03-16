package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.port.secondary.SignatureRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;

public class SignatureRepositoryImpl implements SignatureRepository {

    private final SignatureReadRepository repository;

    public SignatureRepositoryImpl(final SignatureReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(final Signature signature) {
        final SignatureEntity signatureEntity = SignatureEntity.from(signature);
        this.repository.save(signatureEntity);
    }

    @Override
    public Signature get(final ShipmentId shipmentId) {
        return this.repository.findByShipmentId(shipmentId).map(Signature::from).orElse(null);
    }
}
