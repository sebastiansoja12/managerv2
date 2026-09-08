package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import com.warehouse.asyncjob.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.department.api.DepartmentReadModelRebuildApiService;

import java.time.LocalDate;

public class DepartmentReadModelRebuildServiceAdapter implements ReadModelRebuildServicePort {

    private final DepartmentReadModelRebuildApiService rebuildApiService;

    public DepartmentReadModelRebuildServiceAdapter(final DepartmentReadModelRebuildApiService rebuildApiService) {
        this.rebuildApiService = rebuildApiService;
    }

    @Override
    public ReadModelType type() {
        return ReadModelType.DEPARTMENT;
    }

    @Override
    public void rebuild(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo) {
        this.rebuildApiService.rebuildReadModels(operatorId.getValue(), dateFrom, dateTo);
    }
}
