package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.application.port.secondary.SignatureRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.SignaturePersistenceMapper;

import java.util.HashMap;
import java.util.Map;

public class SignatureMockRepositoryImpl implements SignatureRepository {

    private final Map<ShipmentId, SignatureEntity> signatures = new HashMap<>();
    private final SignaturePersistenceMapper persistenceMapper;

    public SignatureMockRepositoryImpl(final SignaturePersistenceMapper persistenceMapper) {
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void save(final Signature signature) {
        final SignatureEntity signatureEntity = this.persistenceMapper.toEntity(signature);
        this.signatures.put(signatureEntity.getShipmentId(), signatureEntity);
    }

    @Override
    public Signature get(final ShipmentId shipmentId) {
        return this.persistenceMapper.toDomain(signatures.get(shipmentId));
    }
}
