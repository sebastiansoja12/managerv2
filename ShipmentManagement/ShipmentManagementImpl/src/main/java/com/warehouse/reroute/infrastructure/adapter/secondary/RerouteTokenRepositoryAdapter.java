package com.warehouse.reroute.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.application.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RerouteTokenPersistenceMapper;

public class RerouteTokenRepositoryAdapter implements RerouteTokenRepository {

    private final RerouteTokenJpaRepository repository;

    private final RerouteTokenPersistenceMapper persistenceMapper;

    public RerouteTokenRepositoryAdapter(final RerouteTokenJpaRepository repository,
                                         final RerouteTokenPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public RerouteToken save(final RerouteToken rerouteToken) {
        return persistenceMapper.toDomain(repository.save(persistenceMapper.toEntity(rerouteToken)));
    }

    @Override
    public Optional<RerouteToken> findLatestByShipmentId(final ShipmentId shipmentId) {
        return repository.findFirstByShipmentIdOrderByCreatedAtDescIdDesc(shipmentId.getValue())
                .map(persistenceMapper::toDomain);
    }

    @Override
    public List<RerouteToken> findActiveByShipmentId(final ShipmentId shipmentId, final Instant now) {
        return repository.findAllByShipmentIdAndUsedAtIsNullAndExpiresAtAfter(shipmentId.getValue(), now)
                .stream()
                .map(persistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteExpiredBefore(final Instant expiredBefore) {
        repository.deleteAllExpiredBefore(expiredBefore);
    }
}
