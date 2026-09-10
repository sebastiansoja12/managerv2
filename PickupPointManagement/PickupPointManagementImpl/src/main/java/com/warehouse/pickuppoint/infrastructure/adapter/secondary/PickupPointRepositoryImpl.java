package com.warehouse.pickuppoint.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointRepository;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointEntity;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.mapper.PickupPointPersistenceMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PickupPointRepositoryImpl implements PickupPointRepository {

    private final OperatorFilteredRepository<PickupPointEntity> repository;
    private final PickupPointPersistenceMapper persistenceMapper;
    private final EntityManager entityManager;

    public PickupPointRepositoryImpl(
            final OperatorFilteredRepository<PickupPointEntity> repository,
            final PickupPointPersistenceMapper persistenceMapper,
            final EntityManager entityManager) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<PickupPoint> findById(final PickupPointId pickupPointId) {
        return findEntity(pickupPointId).map(this.persistenceMapper::toModel);
    }

    @Override
    public Optional<PickupPoint> findByIdForSelection(final PickupPointId pickupPointId) {
        return findEntity(pickupPointId)
                .map(entity -> {
                    this.entityManager.lock(entity, LockModeType.PESSIMISTIC_READ);
                    return this.persistenceMapper.toModel(entity);
                });
    }

    @Override
    public List<PickupPoint> findByIds(final List<PickupPointId> pickupPointIds) {
        if (pickupPointIds.isEmpty()) {
            return List.of();
        }
        final List<UUID> values = pickupPointIds.stream().map(PickupPointId::value).toList();
        return this.repository.createCriteria(PickupPointEntity.class)
                .in("pickupPointId.value", values)
                .list()
                .stream()
                .map(this.persistenceMapper::toModel)
                .toList();
    }

    @Override
    public boolean existsByCode(final PickupPointCode code) {
        return this.repository.createCriteria(PickupPointEntity.class)
                .eq("code", code.value())
                .maxResults(1)
                .one()
                .isPresent();
    }

    @Override
    public PickupPoint save(final PickupPoint pickupPoint) {
        final PickupPointId pickupPointId = pickupPoint.snapshot().pickupPointId();
        final boolean exists = findEntity(pickupPointId).isPresent();
        final PickupPointEntity persistedEntity = exists
                ? this.persistenceMapper.toEntityForUpdate(pickupPoint)
                : this.persistenceMapper.toEntity(pickupPoint);
        if (exists) {
            this.repository.update(persistedEntity);
        } else {
            this.repository.create(persistedEntity);
        }
        this.entityManager.flush();
        final PickupPointEntity savedEntity = findEntity(pickupPointId)
                .orElseThrow(() -> new IllegalStateException(
                        "Saved pickup point was not found: " + pickupPointId.value()));
        return this.persistenceMapper.toModel(savedEntity);
    }

    private Optional<PickupPointEntity> findEntity(final PickupPointId pickupPointId) {
        return this.repository.createCriteria(PickupPointEntity.class)
                .eq("pickupPointId.value", pickupPointId.value())
                .maxResults(1)
                .one();
    }
}
