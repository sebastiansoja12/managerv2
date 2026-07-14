package com.warehouse.organisationstructure.operator.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operator.domain.port.primary.OperatorPort;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;

public class OperatorServiceAdapter implements OperatorApiService {

    private final OperatorPort operatorPort;

    public OperatorServiceAdapter(final OperatorPort operatorPort) {
        this.operatorPort = operatorPort;
    }

    @Override
    public boolean exists(final OperatorId operatorId) {
        return operatorPort.exists(operatorId);
    }

    @Override
    public boolean isActive(final OperatorId operatorId) {
        return operatorPort.isActive(operatorId);
    }

    @Override
    public OperatorDto getById(final OperatorId operatorId) {
        return OperatorMapper.toDto(operatorPort.getById(operatorId));
    }
}
