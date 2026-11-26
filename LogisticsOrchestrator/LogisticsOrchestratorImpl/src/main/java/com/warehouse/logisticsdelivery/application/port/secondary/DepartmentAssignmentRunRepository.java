package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DepartmentAssignmentRunRepository {

    void create(final DepartmentAssignmentRun run);

    void save(final DepartmentAssignmentRun run);

    Optional<DepartmentAssignmentRun> findById(final DepartmentAssignmentRunId runId);

    boolean existsActive(final DepartmentId departmentId);

    Optional<Instant> findLastWatermark(final DepartmentId departmentId);

    boolean claim(final DepartmentAssignmentRunId runId, final String leaseOwner, final Instant now,
                  final Instant leaseUntil);

    List<DepartmentAssignmentRunId> findRecoverable(final Instant pendingUpdatedBefore, final Instant now,
                                                    final int limit);

    int deleteFinishedBefore(final Instant createdBefore);

    List<DepartmentAssignmentRun> findLatest(final int limit);
}
