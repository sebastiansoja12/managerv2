package com.warehouse.logistics.infrastructure.adapter.secondary;

import org.mapstruct.factory.Mappers;

import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.domain.port.secondary.LogisticsRepository;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryEntityMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogisticsRepositoryImpl implements LogisticsRepository {

    private final LogisticsReadRepository repository;

    private final DeliveryEntityMapper mapper = Mappers.getMapper(DeliveryEntityMapper.class);

    @Override
    public LogisticsResponse create(final LogisticsRequest logistics) {
        final DeliveryEntity entity = mapper.map(logistics);
        repository.save(entity);
        return mapper.map(entity);
    }
}
