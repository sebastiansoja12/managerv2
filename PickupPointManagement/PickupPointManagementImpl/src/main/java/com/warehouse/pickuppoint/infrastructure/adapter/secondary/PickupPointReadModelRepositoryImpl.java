package com.warehouse.pickuppoint.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointReadModelRepository;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointReadEntity;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.mapper.PickupPointPersistenceMapper;

public class PickupPointReadModelRepositoryImpl implements PickupPointReadModelRepository {

    private final OperatorFilteredRepository<PickupPointReadEntity> repository;
    private final PickupPointPersistenceMapper persistenceMapper;

    public PickupPointReadModelRepositoryImpl(
            final OperatorFilteredRepository<PickupPointReadEntity> repository,
            final PickupPointPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void sync(final PickupPointSnapshot snapshot) {
        final PickupPointReadEntity readEntity = this.persistenceMapper.toReadEntity(snapshot);
        if (exists(snapshot.pickupPointId())) {
            this.repository.update(readEntity);
        } else {
            this.repository.create(readEntity);
        }
    }

    @Override
    public boolean exists(final PickupPointId pickupPointId) {
        return this.repository.createCriteria(PickupPointReadEntity.class)
                .eq("pickupPointId.value", pickupPointId.value())
                .maxResults(1)
                .one()
                .isPresent();
    }
}
