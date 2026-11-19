package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.supplier.application.port.secondary.OperatorServicePort;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.OutputRequestMapper;

import java.util.Set;

public class OperatorServiceAdapter implements OperatorServicePort {

    private final OperatorApiService operatorApiService;

    public OperatorServiceAdapter(final OperatorApiService operatorApiService) {
        this.operatorApiService = operatorApiService;
    }

    @Override
    public Set<OperatorId> findActiveOperators() {
        final Set<OperatorIdDto> operatorIds = this.operatorApiService.findActiveOperators();
        return OutputRequestMapper.map(operatorIds);
    }
}
