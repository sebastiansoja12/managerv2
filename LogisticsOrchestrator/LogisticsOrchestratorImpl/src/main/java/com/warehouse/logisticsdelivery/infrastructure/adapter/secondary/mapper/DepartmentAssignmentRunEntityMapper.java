package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentWindow;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DepartmentAssignmentRunEntity;
import tools.jackson.databind.json.JsonMapper;

public class DepartmentAssignmentRunEntityMapper {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();

    public DepartmentAssignmentRunEntity toEntity(final DepartmentAssignmentRun run) {
        final DepartmentAssignmentRunSnapshot snapshot = run.snapshot();
        return DepartmentAssignmentRunEntity.builder()
                .runId(snapshot.runId())
                .version(snapshot.version())
                .departmentId(snapshot.departmentId())
                .dateField(snapshot.dateField())
                .windowFrom(snapshot.window().from())
                .windowTo(snapshot.window().to())
                .backlogFrom(snapshot.window().backlogFrom())
                .batchSize(snapshot.batchSize())
                .status(snapshot.status())
                .cursorDate(snapshot.cursor() == null ? null : snapshot.cursor().eventTime())
                .cursorId(snapshot.cursor() == null ? null : snapshot.cursor().deliveryId().getId())
                .maxEligibleAt(snapshot.maxEligibleAt())
                .watermark(snapshot.watermark())
                .retries(snapshot.retries())
                .leaseOwner(snapshot.leaseOwner())
                .leaseUntil(snapshot.leaseUntil())
                .summary(JSON_MAPPER.writeValueAsString(snapshot.summary()))
                .error(snapshot.error())
                .createdAt(snapshot.createdAt())
                .updatedAt(snapshot.updatedAt())
                .build();
    }

    public DepartmentAssignmentRun toDomain(final DepartmentAssignmentRunEntity entity) {
        return new DepartmentAssignmentRun(entity.getRunId(), entity.getVersion(), entity.getDepartmentId(),
                entity.getDateField(), new AssignmentWindow(entity.getWindowFrom(), entity.getWindowTo(),
                entity.getBacklogFrom()), entity.getBatchSize(), entity.getStatus(),
                entity.getCursorId() == null ? null
                        : new DeliveryAssignmentCursor(entity.getCursorDate(), new DeliveryId(entity.getCursorId())),
                entity.getMaxEligibleAt(), entity.getWatermark(), entity.getRetries(), entity.getLeaseOwner(),
                entity.getLeaseUntil(), entity.getSummary() == null ? AssignmentRunSummary.empty()
                        : JSON_MAPPER.readValue(entity.getSummary(), AssignmentRunSummary.class),
                entity.getError(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
