package com.warehouse.platformadministration.infrastructure.adapter.primary.mapper;

import com.warehouse.platformadministration.domain.vo.DepartmentAssignmentRunView;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.DepartmentAssignmentRunResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorJobRunResponse;

public final class OperatorAdminActionResponseMapper {

    private OperatorAdminActionResponseMapper() {
    }

    public static OperatorJobRunResponse toResponse(final OperatorJobRun run) {
        return new OperatorJobRunResponse(run.jobType().name(), run.asynchronous(), run.affected(), run.total());
    }

    public static DepartmentAssignmentRunResponse toResponse(final DepartmentAssignmentRunView run) {
        return new DepartmentAssignmentRunResponse(run.runId().value(), run.departmentId().getValue(), run.windowFrom(),
                run.windowTo(), run.backlogFrom(), run.status(), run.retries(), run.watermark(), run.discovered(),
                run.eligible(), run.assigned(), run.noCourier(), run.skippedNotAtDepartment(), run.skippedConcurrent(),
                run.batches(), run.error(), run.createdAt(), run.updatedAt());
    }
}
