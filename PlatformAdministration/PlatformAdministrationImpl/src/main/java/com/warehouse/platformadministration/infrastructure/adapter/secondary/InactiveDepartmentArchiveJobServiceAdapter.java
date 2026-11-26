package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.platformadministration.application.port.secondary.OperatorJobServicePort;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

public class InactiveDepartmentArchiveJobServiceAdapter implements OperatorJobServicePort {

    private final DepartmentApiService departmentApiService;

    private final OperatorScopeExecutor operatorScopeExecutor;

    public InactiveDepartmentArchiveJobServiceAdapter(final DepartmentApiService departmentApiService,
                                                      final OperatorScopeExecutor operatorScopeExecutor) {
        this.departmentApiService = departmentApiService;
        this.operatorScopeExecutor = operatorScopeExecutor;
    }

    @Override
    public OperatorJobType jobType() {
        return OperatorJobType.INACTIVE_DEPARTMENT_ARCHIVE;
    }

    @Override
    public OperatorJobRun run(final OperatorId operatorId) {
        final int archived = operatorScopeExecutor.execute(new OperatorScope(operatorId, null),
                departmentApiService::archiveInactiveDepartments);
        return new OperatorJobRun(jobType(), false, archived, null);
    }
}
