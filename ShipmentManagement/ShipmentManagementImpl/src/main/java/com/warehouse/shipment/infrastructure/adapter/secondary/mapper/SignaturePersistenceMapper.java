package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;

public class SignaturePersistenceMapper {

    public Signature toDomain(final SignatureEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Signature(entity.getSignatureId(), entity.getSignerName(), entity.getSignedAt(), entity.getSignatureMethod(),
                entity.getDocumentReference(), entity.getShipmentId(), entity.getSignature());
    }

    public SignatureEntity toEntity(final Signature signature) {
        if (signature == null) {
            return null;
        }
        return new SignatureEntity(signature.getSignatureId(), signature.getSignerName(), signature.getSignedAt(), signature.getSignatureMethod(),
                signature.getDocumentReference(), signature.getShipmentId(), signature.getSignature());
    }
}
