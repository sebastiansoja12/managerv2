package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.ConditionalUpdate;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentRunRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DepartmentAssignmentRunEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DepartmentAssignmentRunEntityMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class DepartmentAssignmentRunRepositoryImpl implements DepartmentAssignmentRunRepository {

    private static final List<DepartmentAssignmentRunStatus> ACTIVE_STATUSES =
            List.of(DepartmentAssignmentRunStatus.PENDING, DepartmentAssignmentRunStatus.PROCESSING);

    private static final List<DepartmentAssignmentRunStatus> FINISHED_STATUSES = List.of(
            DepartmentAssignmentRunStatus.COMPLETED, DepartmentAssignmentRunStatus.FAILED,
            DepartmentAssignmentRunStatus.ERROR);

    private final OperatorFilteredRepository<DepartmentAssignmentRunEntity> repository;

    private final DepartmentAssignmentRunEntityMapper mapper;

    public DepartmentAssignmentRunRepositoryImpl(
            final OperatorFilteredRepository<DepartmentAssignmentRunEntity> repository,
            final DepartmentAssignmentRunEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void create(final DepartmentAssignmentRun run) {
        final DepartmentAssignmentRunEntity entity = mapper.toEntity(run);
        repository.create(entity);
        run.applyPersistedVersion(entity.getVersion());
    }

    @Override
    public void save(final DepartmentAssignmentRun run) {
        run.applyPersistedVersion(repository.updateAndFlush(mapper.toEntity(run)).getVersion());
    }

    @Override
    public Optional<DepartmentAssignmentRun> findById(final DepartmentAssignmentRunId runId) {
        return repository.createCriteria(DepartmentAssignmentRunEntity.class)
                .eq("runId.value", runId.value())
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsActive(final DepartmentId departmentId) {
        return repository.createCriteria(DepartmentAssignmentRunEntity.class)
                .eq("departmentId.value", departmentId.getValue())
                .in("status", ACTIVE_STATUSES)
                .count() > 0;
    }

    @Override
    public Optional<Instant> findLastWatermark(final DepartmentId departmentId) {
        return repository.createCriteria(DepartmentAssignmentRunEntity.class)
                .eq("departmentId.value", departmentId.getValue())
                .eq("status", DepartmentAssignmentRunStatus.COMPLETED)
                .isNotNull("watermark")
                .desc("watermark")
                .maxResults(1)
                .one()
                .map(DepartmentAssignmentRunEntity::getWatermark);
    }

    @Override
    public boolean claim(final DepartmentAssignmentRunId runId, final String leaseOwner, final Instant now,
                         final Instant leaseUntil) {
        final ConditionalUpdate<DepartmentAssignmentRunEntity> update =
                repository.createUpdate(DepartmentAssignmentRunEntity.class)
                        .set("status", DepartmentAssignmentRunStatus.PROCESSING)
                        .set("leaseOwner", leaseOwner)
                        .set("leaseUntil", leaseUntil)
                        .set("updatedAt", now)
                        .increment("version")
                        .eq("runId.value", runId.value())
                        .in("status", ACTIVE_STATUSES);
        final CriteriaBuilder builder = update.getCriteriaBuilder();
        final Root<DepartmentAssignmentRunEntity> root = update.getRoot();
        update.and(builder.or(builder.isNull(root.get("leaseUntil")),
                builder.lessThan(root.get("leaseUntil"), now)));
        return update.execute() == 1;
    }

    @Override
    public List<DepartmentAssignmentRunId> findRecoverable(final Instant pendingUpdatedBefore, final Instant now,
                                                           final int limit) {
        final Criteria<DepartmentAssignmentRunEntity> criteria =
                repository.createCriteria(DepartmentAssignmentRunEntity.class);
        final CriteriaBuilder builder = criteria.getCriteriaBuilder();
        final Root<DepartmentAssignmentRunEntity> root = criteria.getRoot();
        criteria.and(builder.or(
                builder.and(builder.equal(root.get("status"), DepartmentAssignmentRunStatus.PENDING),
                        builder.lessThan(root.get("updatedAt"), pendingUpdatedBefore)),
                builder.and(builder.equal(root.get("status"), DepartmentAssignmentRunStatus.PROCESSING),
                        builder.lessThan(root.get("leaseUntil"), now))));
        return criteria.asc("createdAt")
                .maxResults(limit)
                .list()
                .stream()
                .map(DepartmentAssignmentRunEntity::getRunId)
                .toList();
    }

    @Override
    public int deleteFinishedBefore(final Instant createdBefore) {
        final List<DepartmentAssignmentRunEntity> finished = repository.createCriteria(DepartmentAssignmentRunEntity.class)
                .in("status", FINISHED_STATUSES)
                .lt("createdAt", createdBefore)
                .list();
        finished.forEach(repository::delete);
        return finished.size();
    }

    @Override
    public List<DepartmentAssignmentRun> findLatest(final int limit) {
        return repository.createCriteria(DepartmentAssignmentRunEntity.class)
                .desc("createdAt")
                .maxResults(limit)
                .list()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
