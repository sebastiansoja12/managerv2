package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.model.OperatorDataErasure;

import java.util.Optional;

public interface OperatorDataErasureRepository {

    OperatorDataErasure save(final OperatorDataErasure erasure);

    Optional<OperatorDataErasure> findLatest(final OperatorId operatorId);
}
