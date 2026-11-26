package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.api.DepartmentAssignmentApiService;
import com.warehouse.platformadministration.application.port.secondary.OperatorJobServicePort;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;

public class CourierAssignmentRunCleanupJobServiceAdapter implements OperatorJobServicePort {

    private final DepartmentAssignmentApiService departmentAssignmentApiService;

    public CourierAssignmentRunCleanupJobServiceAdapter(final DepartmentAssignmentApiService departmentAssignmentApiService) {
        this.departmentAssignmentApiService = departmentAssignmentApiService;
    }

    @Override
    public OperatorJobType jobType() {
        return OperatorJobType.COURIER_ASSIGNMENT_RUN_CLEANUP;
    }

    @Override
    public OperatorJobRun run(final OperatorId operatorId) {
        return new OperatorJobRun(jobType(), false, departmentAssignmentApiService.cleanup(operatorId), null);
    }
}
