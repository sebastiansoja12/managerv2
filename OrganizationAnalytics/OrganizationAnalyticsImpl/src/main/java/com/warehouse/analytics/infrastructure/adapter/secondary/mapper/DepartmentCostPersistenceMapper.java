package com.warehouse.analytics.infrastructure.adapter.secondary.mapper;

import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.infrastructure.adapter.secondary.entity.DepartmentCostEntity;

public class DepartmentCostPersistenceMapper {

    public DepartmentCostEntity toEntity(final DepartmentCost departmentCost) {
        return new DepartmentCostEntity(departmentCost.costId(), departmentCost.departmentCode(),
                departmentCost.category(), departmentCost.amount(), departmentCost.currency(),
                departmentCost.incurredOn(), departmentCost.description(), departmentCost.recordedBy(),
                departmentCost.recordedAt());
    }

    public DepartmentCost toDomain(final DepartmentCostEntity entity) {
        return new DepartmentCost(entity.getCostId(), entity.getDepartmentCode(), entity.getCategory(),
                entity.getAmount(), entity.getCurrency(), entity.getIncurredOn(), entity.getDescription(),
                entity.getRecordedBy(), entity.getRecordedAt());
    }
}
