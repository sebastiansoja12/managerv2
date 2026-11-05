package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.supplier.domain.port.secondary.OperatorServicePort;

import java.util.Set;
import java.util.stream.Collectors;

public class OperatorServiceAdapter implements OperatorServicePort {

    private final OperatorApiService operatorApiService;

    public OperatorServiceAdapter(final OperatorApiService operatorApiService) {
        this.operatorApiService = operatorApiService;
    }

    @Override
    public Set<OperatorId> findActiveOperators() {
        final Set<OperatorIdDto> operatorIds = this.operatorApiService.findActiveOperators();
        return operatorIds
                .stream()
                .map(operatorId -> new OperatorId(operatorId.value()))
                .collect(Collectors.toSet());
    }
}
