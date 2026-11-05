package com.warehouse.organisationstructure.api;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;

import java.util.Set;

public interface OperatorApiService {
    boolean exists(final OperatorId operatorId);
    boolean isActive(final OperatorId operatorId);
    OperatorDto getById(final OperatorId operatorId);
    Set<OperatorIdDto> findActiveOperators();
}
