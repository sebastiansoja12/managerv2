package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper;

import com.warehouse.logisticsdelivery.api.dto.DepartmentAssignmentRunDto;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;

public class DepartmentAssignmentRunApiMapper {

    public DepartmentAssignmentRunDto toDto(final DepartmentAssignmentRunSnapshot run) {
        return new DepartmentAssignmentRunDto(run.runId(), run.departmentId(), run.window().from(), run.window().to(),
                run.window().backlogFrom(), run.status().name(), run.retries(), run.watermark(),
                run.summary().discovered(), run.summary().eligible(), run.summary().assigned(),
                run.summary().noCourier(), run.summary().skippedNotAtDepartment(), run.summary().skippedConcurrent(),
                run.summary().batches(), run.error(), run.createdAt(), run.updatedAt());
    }
}
