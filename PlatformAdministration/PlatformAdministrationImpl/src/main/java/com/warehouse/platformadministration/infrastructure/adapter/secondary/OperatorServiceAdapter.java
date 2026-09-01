package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

import java.util.Optional;

public class OperatorServiceAdapter implements OperatorServicePort {

    private final OperatorApiService operatorApiService;

    public OperatorServiceAdapter(final OperatorApiService operatorApiService) {
        this.operatorApiService = operatorApiService;
    }

    @Override
    public Optional<OperatorScope> findScope(final OperatorId operatorId) {
        if (!operatorApiService.exists(operatorId)) {
            return Optional.empty();
        }
        return Optional.of(operatorApiService.getById(operatorId))
                .map(operator -> new OperatorScope(operatorId, operator.registeringUserId()));
    }

    @Override
    public Optional<OperatorProfile> findProfile(final OperatorId operatorId) {
        if (!operatorApiService.exists(operatorId)) {
            return Optional.empty();
        }
        return Optional.of(operatorApiService.getById(operatorId))
                .map(operator -> new OperatorProfile(operatorId, operator.companyName(),
                        operator.taxId() != null ? operator.taxId().value() : null, operator.contactEmail(),
                        operator.contactPhone(), operator.contractStartDate(), operator.contractEndDate(),
                        operator.foundedDate(), operator.status() != null ? operator.status().name() : null,
                        operator.createdAt()));
    }
}
