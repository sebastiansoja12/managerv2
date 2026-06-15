package com.warehouse.deliverynetwork.infrastructure.adapter.secondary;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.deliverynetwork.application.port.secondary.DeliveryNetworkRepository;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkEntity;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper.DeliveryNetworkPersistenceMapper;

import java.util.Optional;

public class DeliveryNetworkRepositoryImpl implements DeliveryNetworkRepository {

    private final OperatorFilteredRepository<DeliveryNetworkEntity> repository;

    private final DeliveryNetworkPersistenceMapper persistenceMapper;

    public DeliveryNetworkRepositoryImpl(
            final OperatorFilteredRepository<DeliveryNetworkEntity> repository,
            final DeliveryNetworkPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public Optional<DeliveryNetwork> find() {
        return findEntity().map(this.persistenceMapper::toModel);
    }

    @Override
    public void save(final DeliveryNetwork deliveryNetwork) {
        findEntity().ifPresentOrElse(
                entity -> {
                    this.persistenceMapper.updateEntity(entity, deliveryNetwork);
                    this.repository.update(entity);
                },
                () -> this.repository.create(this.persistenceMapper.toEntity(deliveryNetwork)));
    }

    private Optional<DeliveryNetworkEntity> findEntity() {
        return this.repository.createCriteria(DeliveryNetworkEntity.class)
                .maxResults(1)
                .one();
    }
}
