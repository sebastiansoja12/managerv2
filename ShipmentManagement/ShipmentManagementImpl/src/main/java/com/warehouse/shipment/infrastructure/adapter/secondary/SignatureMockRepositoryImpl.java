package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.port.secondary.SignatureRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;

import java.util.HashMap;
import java.util.Map;

public class SignatureMockRepositoryImpl implements SignatureRepository {

    private final Map<ShipmentId, SignatureEntity> signatures = new HashMap<>();

    @Override
    public void save(final Signature signature) {
        final SignatureEntity signatureEntity = SignatureEntity.from(signature);
        this.signatures.put(signatureEntity.getShipmentId(), signatureEntity);
    }

    @Override
    public Signature get(final ShipmentId shipmentId) {
        return Signature.from(signatures.get(shipmentId));
    }
}
