package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.logisticsdelivery.domain.model.Signature;

import java.util.Optional;

public interface SignatureRepository {
    void save(final Signature signature);

    Optional<Signature> findById(final SignatureId signatureId);
}
