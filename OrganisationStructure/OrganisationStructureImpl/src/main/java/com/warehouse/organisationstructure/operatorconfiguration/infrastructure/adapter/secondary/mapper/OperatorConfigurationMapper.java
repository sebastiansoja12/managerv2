package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.OperatorConfigurationEntity;

public final class OperatorConfigurationMapper {

    private OperatorConfigurationMapper() {
    }

    public static OperatorConfigurationEntity toEntity(final OperatorId operatorId,
                                                       final OperatorConfiguration configuration) {
        return OperatorConfigurationEntity.fromModel(operatorId, configuration);
    }

    public static OperatorConfiguration toModel(final OperatorConfigurationEntity entity) {
        if (entity == null) {
            return null;
        }
        return entity.toModel();
    }
}
