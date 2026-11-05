package com.warehouse.fleettracking.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.fleettracking.application.port.secondary.TrackingSessionRepository;
import com.warehouse.fleettracking.domain.enumeration.TrackingSessionStatus;
import com.warehouse.fleettracking.domain.model.TrackingSession;
import com.warehouse.fleettracking.domain.vo.PositionFix;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.entity.TrackingPositionEntity;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.entity.TrackingSessionEntity;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.mapper.TrackingSessionPersistenceMapper;

import java.util.List;
import java.util.Optional;

public class TrackingSessionRepositoryImpl implements TrackingSessionRepository {

    private final OperatorFilteredRepository<TrackingSessionEntity> sessionRepository;

    private final OperatorFilteredRepository<TrackingPositionEntity> positionRepository;

    private final TrackingSessionPersistenceMapper persistenceMapper;

    public TrackingSessionRepositoryImpl(final OperatorFilteredRepository<TrackingSessionEntity> sessionRepository,
                                         final OperatorFilteredRepository<TrackingPositionEntity> positionRepository,
                                         final TrackingSessionPersistenceMapper persistenceMapper) {
        this.sessionRepository = sessionRepository;
        this.positionRepository = positionRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void create(final TrackingSession trackingSession) {
        this.sessionRepository.create(this.persistenceMapper.toEntity(trackingSession));
        appendPositions(trackingSession);
    }

    @Override
    public void update(final TrackingSession trackingSession) {
        this.sessionRepository.update(this.persistenceMapper.toEntity(trackingSession));
        appendPositions(trackingSession);
    }

    @Override
    public Optional<TrackingSession> findById(final TrackingSessionId trackingSessionId) {
        return this.sessionRepository.createCriteria(TrackingSessionEntity.class)
                .eq("trackingSessionId.value", trackingSessionId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public List<TrackingSession> findActiveByDriver(final UserId driverId) {
        return this.sessionRepository.createCriteria(TrackingSessionEntity.class)
                .eq("status", TrackingSessionStatus.ACTIVE)
                .eq("driverUserId.value", driverId.value())
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<TrackingSession> findActive() {
        return this.sessionRepository.createCriteria(TrackingSessionEntity.class)
                .eq("status", TrackingSessionStatus.ACTIVE)
                .desc("startedAt")
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<PositionFix> findPositions(final TrackingSessionId trackingSessionId) {
        return this.positionRepository.createCriteria(TrackingPositionEntity.class)
                .eq("trackingSessionId.value", trackingSessionId.value())
                .asc("position.sequence")
                .list()
                .stream()
                .map(entity -> this.persistenceMapper.toDomain(entity.getPosition()))
                .toList();
    }

    private void appendPositions(final TrackingSession trackingSession) {
        trackingSession.acceptedPositions().forEach(position ->
                this.positionRepository.create(this.persistenceMapper.toEntity(trackingSession, position)));
    }
}
