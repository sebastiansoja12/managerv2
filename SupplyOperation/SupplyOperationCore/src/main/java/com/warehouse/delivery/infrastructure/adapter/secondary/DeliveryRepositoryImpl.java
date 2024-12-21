package com.warehouse.delivery.infrastructure.adapter.secondary;

import org.mapstruct.factory.Mappers;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryEntityMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final DeliveryReadRepository repository;

    private final DeliveryEntityMapper mapper = Mappers.getMapper(DeliveryEntityMapper.class);

    @Override
    public DeliveryResponse create(final DeliveryRequest delivery) {
        final DeliveryEntity entity = mapper.map(delivery);
        repository.save(entity);
        return mapper.map(entity);
    }
}
