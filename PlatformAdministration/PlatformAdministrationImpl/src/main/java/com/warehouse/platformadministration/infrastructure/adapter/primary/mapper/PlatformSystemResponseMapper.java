package com.warehouse.platformadministration.infrastructure.adapter.primary.mapper;

import com.warehouse.platformadministration.domain.model.MaintenanceMode;
import com.warehouse.platformadministration.domain.vo.OperatorAccess;
import com.warehouse.platformadministration.domain.vo.ReadModelRebuildJob;
import com.warehouse.platformadministration.domain.vo.SystemStatus;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.MaintenanceModeResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorAccessResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.PublicMaintenanceResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.ReadModelRebuildJobResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.SystemStatusResponse;

public final class PlatformSystemResponseMapper {

    private PlatformSystemResponseMapper() {
    }

    public static MaintenanceModeResponse toResponse(final MaintenanceMode maintenanceMode) {
        return new MaintenanceModeResponse(maintenanceMode.enabled(), maintenanceMode.message(),
                maintenanceMode.updatedBy(), maintenanceMode.updatedAt());
    }

    public static PublicMaintenanceResponse toPublicResponse(final MaintenanceMode maintenanceMode) {
        return new PublicMaintenanceResponse(maintenanceMode.enabled(),
                maintenanceMode.enabled() ? maintenanceMode.message() : null);
    }

    public static OperatorAccessResponse toResponse(final OperatorAccess operatorAccess) {
        return new OperatorAccessResponse(operatorAccess.operatorId().value(), operatorAccess.activeSessions());
    }

    public static ReadModelRebuildJobResponse toResponse(final ReadModelRebuildJob job) {
        return new ReadModelRebuildJobResponse(job.jobId(), job.readModelType(), job.status(), job.dateFrom(),
                job.dateTo(), job.operatorId() != null ? job.operatorId().value() : null, job.createdAt(),
                job.startedAt(), job.finishedAt(), job.error());
    }

    public static SystemStatusResponse toResponse(final SystemStatus status) {
        return new SystemStatusResponse(
                new SystemStatusResponse.Runtime(status.runtime().applicationName(), status.runtime().version(),
                        status.runtime().javaVersion(), status.runtime().startedAt(), status.runtime().uptimeSeconds(),
                        status.runtime().activeProfiles(), status.runtime().usedMemoryMegabytes(),
                        status.runtime().maxMemoryMegabytes(), status.runtime().availableProcessors()),
                new SystemStatusResponse.Database(status.database().available(), status.database().product(),
                        status.database().version(), status.database().responseTimeMillis()),
                new SystemStatusResponse.Migrations(status.migrations().executedChangeSets(),
                        status.migrations().lastChangeSetId(), status.migrations().lastAuthor(),
                        status.migrations().lastExecutedAt()),
                toResponse(status.maintenance()));
    }
}
