package com.warehouse.create.infrastructure.adapter.secondary;

import com.warehouse.create.domain.model.DeliveryCreate;
import com.warehouse.create.domain.model.Request;
import com.warehouse.create.domain.port.secondary.DeliveryCreateRepository;
import com.warehouse.create.infrastructure.adapter.secondary.entity.DeliveryCreateEntity;
import com.warehouse.create.infrastructure.adapter.secondary.mapper.DeliveryCreateEntityMapper;
import com.warehouse.create.infrastructure.adapter.secondary.mapper.DeliveryCreateModelMapper;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class DeliveryCreateRepositoryImpl implements DeliveryCreateRepository {

    private final DeliveryCreateReadRepository repository;

    private final DeliveryCreateEntityMapper deliveryCreateEntityMapper = getMapper(DeliveryCreateEntityMapper.class);

    private final DeliveryCreateModelMapper deliveryCreateModelMapper = getMapper(DeliveryCreateModelMapper.class);

    @Override
    public DeliveryCreate save(Request request) {
        final DeliveryCreateEntity deliveryCreate = deliveryCreateEntityMapper.map(request);
        repository.save(deliveryCreate);
        return deliveryCreateModelMapper.map(deliveryCreate);
    }
}
