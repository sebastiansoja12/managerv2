package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryTarget;
import com.warehouse.logistics.domain.port.secondary.LogisticsRepository;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryEntityMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class LogisticsRepositoryImpl implements LogisticsRepository {

    private final OperatorFilteredRepository<DeliveryEntity> repository;
    private final DeliveryEntityMapper mapper = Mappers.getMapper(DeliveryEntityMapper.class);

    public LogisticsRepositoryImpl(final OperatorFilteredRepository<DeliveryEntity> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Delivery> findById(final DeliveryId deliveryId) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("id.id", deliveryId.getId())
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Delivery> findByTargetAndType(final DeliveryTarget target, final DeliveryType type) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("targetType", target.type())
                .eq("targetId", target.id())
                .eq("type", type)
                .maxResults(1)
                .one()
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findRecent(final int offset, final int limit) {
        return repository.createCriteria(DeliveryEntity.class)
                .desc("created")
                .firstResult(offset)
                .maxResults(limit)
                .list().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void createOrUpdate(final Delivery delivery) {
        final DeliveryEntity entity = mapper.toEntity(delivery);
        if (deliveryExists(delivery.getDeliveryId())) {
            repository.update(entity);
        } else {
            repository.create(entity);
        }
    }

    @Override
    public Optional<Delivery> findByShipmentId(final ShipmentId shipmentId) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("shipmentId", shipmentId.getValue())
                .one()
                .map(mapper::toDomain);
    }

    private boolean deliveryExists(final DeliveryId deliveryId) {
        return deliveryId != null && repository.createCriteria(DeliveryEntity.class)
                .eq("id.id", deliveryId.getId())
                .one()
                .isPresent();
    }

}
