package com.warehouse.department.infrastructure.adapter.primary;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.department.api.DepartmentReadModelRebuildApiService;
import com.warehouse.department.domain.service.DepartmentSyncService;

@Service
public class DepartmentReadModelRebuildApiServiceAdapter implements DepartmentReadModelRebuildApiService {

    private final DepartmentSyncService departmentSyncService;
    private final OperatorContext operatorContext;

    public DepartmentReadModelRebuildApiServiceAdapter(final DepartmentSyncService departmentSyncService,
                                                       final OperatorContext operatorContext) {
        this.departmentSyncService = departmentSyncService;
        this.operatorContext = operatorContext;
    }

    @Override
    public int rebuildReadModels(final Long operatorId,
                                 final LocalDate dateFrom,
                                 final LocalDate dateTo) {
        return this.operatorContext.runAs(OperatorId.of(operatorId), this.departmentSyncService::syncReadModels);
    }
}
