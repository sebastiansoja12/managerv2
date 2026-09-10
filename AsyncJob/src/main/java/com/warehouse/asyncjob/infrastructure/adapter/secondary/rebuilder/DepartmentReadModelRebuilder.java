package com.warehouse.asyncjob.infrastructure.adapter.secondary.rebuilder;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.warehouse.asyncjob.domain.model.ReadModelType;
import com.warehouse.asyncjob.domain.service.ReadModelRebuilder;
import com.warehouse.department.api.DepartmentReadModelRebuildApiService;

@Component
public class DepartmentReadModelRebuilder implements ReadModelRebuilder {

    private final DepartmentReadModelRebuildApiService rebuildApiService;

    public DepartmentReadModelRebuilder(final DepartmentReadModelRebuildApiService rebuildApiService) {
        this.rebuildApiService = rebuildApiService;
    }

    @Override
    public ReadModelType type() {
        return ReadModelType.DEPARTMENT;
    }

    @Override
    public void rebuild(final Long operatorId,
                        final LocalDate dateFrom,
                        final LocalDate dateTo) {
        this.rebuildApiService.rebuildReadModels(operatorId, dateFrom, dateTo);
    }
}
