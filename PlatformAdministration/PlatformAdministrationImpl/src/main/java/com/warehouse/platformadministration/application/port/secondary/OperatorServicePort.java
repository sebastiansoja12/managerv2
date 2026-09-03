package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

import java.util.Optional;

public interface OperatorServicePort {

    Optional<OperatorScope> findScope(final OperatorId operatorId);

    Optional<OperatorProfile> findProfile(final OperatorId operatorId);
}
