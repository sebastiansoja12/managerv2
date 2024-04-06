package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper.DeliveryMissedToEntityMapper;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper.DeliveryMissedToModelMapper;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;


@AllArgsConstructor
public class DeliveryMissedRepositoryImpl implements DeliveryMissedRepository {

    private final DeliveryMissedReadRepository deliveryMissedReadRepository;

    private final DeliveryMissedToEntityMapper toEntityMapper = getMapper(DeliveryMissedToEntityMapper.class);

    private final DeliveryMissedToModelMapper toModelMapper = getMapper(DeliveryMissedToModelMapper.class);

    @Override
    public DeliveryMissed saveDeliveryMissed(DeliveryMissedRequest request) {
        final DeliveryMissedEntity deliveryMissedEntity = toEntityMapper.map(request);
        deliveryMissedReadRepository.save(deliveryMissedEntity);
        return toModelMapper.map(deliveryMissedEntity);
    }
}
