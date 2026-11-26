package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.api.DepartmentAssignmentApiService;
import com.warehouse.logisticsdelivery.api.dto.AssignmentOrchestrationDto;
import com.warehouse.platformadministration.application.port.secondary.OperatorJobServicePort;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;

public class CourierAssignmentOrchestrationJobServiceAdapter implements OperatorJobServicePort {

    private final DepartmentAssignmentApiService departmentAssignmentApiService;

    public CourierAssignmentOrchestrationJobServiceAdapter(final DepartmentAssignmentApiService departmentAssignmentApiService) {
        this.departmentAssignmentApiService = departmentAssignmentApiService;
    }

    @Override
    public OperatorJobType jobType() {
        return OperatorJobType.COURIER_ASSIGNMENT_ORCHESTRATION;
    }

    @Override
    public OperatorJobRun run(final OperatorId operatorId) {
        final AssignmentOrchestrationDto orchestration = departmentAssignmentApiService.orchestrate(operatorId);
        return new OperatorJobRun(jobType(), true, orchestration.triggeredDepartments(),
                orchestration.configuredDepartments());
    }
}
