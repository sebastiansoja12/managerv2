package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.application.port.secondary.SignatureRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.SignaturePersistenceMapper;

public class SignatureRepositoryImpl implements SignatureRepository {

    private final SignatureReadRepository repository;
    private final SignaturePersistenceMapper persistenceMapper;

    public SignatureRepositoryImpl(final SignatureReadRepository repository,
                                   final SignaturePersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void save(final Signature signature) {
        final SignatureEntity signatureEntity = this.persistenceMapper.toEntity(signature);
        this.repository.save(signatureEntity);
    }

    @Override
    public Signature get(final ShipmentId shipmentId) {
        return this.repository.findByShipmentId(shipmentId).map(this.persistenceMapper::toDomain).orElse(null);
    }
}
