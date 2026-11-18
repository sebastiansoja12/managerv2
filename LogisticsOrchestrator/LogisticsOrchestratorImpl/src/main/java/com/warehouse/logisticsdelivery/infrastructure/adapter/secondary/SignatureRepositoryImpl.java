package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.logisticsdelivery.application.port.secondary.SignatureRepository;
import com.warehouse.logisticsdelivery.domain.model.Signature;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.SignatureEntity;

import java.util.Optional;

public class SignatureRepositoryImpl implements SignatureRepository {

    private final SignatureReadRepository repository;

    public SignatureRepositoryImpl(final SignatureReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(final Signature signature) {
        this.repository.save(new SignatureEntity(signature.getSignatureId(), signature.getSignerName(),
                signature.getSignedAt(), signature.getSignatureMethod(), signature.getDocumentReference(),
                signature.getSignature()));
    }

    @Override
    public Optional<Signature> findById(final SignatureId signatureId) {
        return this.repository.findById(signatureId)
                .map(entity -> new Signature(entity.getSignatureId(), entity.getSignerName(), entity.getSignedAt(),
                        entity.getSignatureMethod(), entity.getDocumentReference(), entity.getSignature()));
    }
}
