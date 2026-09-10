package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary.mapper;

import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

public final class OperatorConfigurationEventMapper {

    private OperatorConfigurationEventMapper() {
    }

    public static OperatorConfiguration toModel(final OperatorConfigurationDto configuration) {
        return OperatorMapper.toModelConfiguration(configuration);
    }
}
