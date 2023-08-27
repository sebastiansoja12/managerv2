package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryEntityMapper;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;

@AllArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final DeliveryReadRepository repository;

    private final DeliveryEntityMapper mapper = Mappers.getMapper(DeliveryEntityMapper.class);

    @Override
    public Delivery saveDelivery(Delivery delivery) {
        final DeliveryEntity entity = mapper.map(delivery);
        repository.save(entity);
        return mapper.map(entity);
    }
}
