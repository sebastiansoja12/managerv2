package com.warehouse.logistics.infrastructure.adapter.secondary;

import org.mapstruct.factory.Mappers;

import com.warehouse.logistics.domain.model.DeliveryRequest;
import com.warehouse.logistics.domain.model.DeliveryResponse;
import com.warehouse.logistics.domain.port.secondary.LogisticsRepository;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryEntityMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogisticsRepositoryImpl implements LogisticsRepository {

    private final LogisticsReadRepository repository;

    private final DeliveryEntityMapper mapper = Mappers.getMapper(DeliveryEntityMapper.class);

    @Override
    public DeliveryResponse create(final DeliveryRequest delivery) {
        final DeliveryEntity entity = mapper.map(delivery);
        repository.save(entity);
        return mapper.map(entity);
    }
}
