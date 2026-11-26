package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.api.DepartmentAssignmentApiService;
import com.warehouse.logisticsdelivery.api.dto.DepartmentAssignmentRunDto;
import com.warehouse.platformadministration.application.port.secondary.DepartmentAssignmentRunServicePort;
import com.warehouse.platformadministration.domain.vo.DepartmentAssignmentRunView;

import java.util.List;

public class DepartmentAssignmentRunServiceAdapter implements DepartmentAssignmentRunServicePort {

    private final DepartmentAssignmentApiService departmentAssignmentApiService;

    public DepartmentAssignmentRunServiceAdapter(final DepartmentAssignmentApiService departmentAssignmentApiService) {
        this.departmentAssignmentApiService = departmentAssignmentApiService;
    }

    @Override
    public List<DepartmentAssignmentRunView> findLatest(final OperatorId operatorId, final int limit) {
        return departmentAssignmentApiService.findLatestRuns(operatorId, limit).stream()
                .map(this::toView)
                .toList();
    }

    private DepartmentAssignmentRunView toView(final DepartmentAssignmentRunDto run) {
        return new DepartmentAssignmentRunView(run.runId(), run.departmentId(), run.windowFrom(), run.windowTo(),
                run.backlogFrom(), run.status(), run.retries(), run.watermark(), run.discovered(), run.eligible(),
                run.assigned(), run.noCourier(), run.skippedNotAtDepartment(), run.skippedConcurrent(), run.batches(),
                run.error(), run.createdAt(), run.updatedAt());
    }
}
