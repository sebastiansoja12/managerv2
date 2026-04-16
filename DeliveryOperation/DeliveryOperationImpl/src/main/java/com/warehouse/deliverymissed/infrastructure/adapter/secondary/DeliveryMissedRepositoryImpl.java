package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedRepository;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper.DeliveryMissedToEntityMapper;


public class DeliveryMissedRepositoryImpl implements DeliveryMissedRepository {

    private final DeliveryMissedReadRepository deliveryMissedReadRepository;

    private final DeliveryMissedToEntityMapper toEntityMapper = getMapper(DeliveryMissedToEntityMapper.class);

    public DeliveryMissedRepositoryImpl(final DeliveryMissedReadRepository deliveryMissedReadRepository) {
        this.deliveryMissedReadRepository = deliveryMissedReadRepository;
    }

    @Override
    public DeliveryMissed saveDeliveryMissed(DeliveryMissedRequest request) {
        final DeliveryMissedEntity deliveryMissedEntity = toEntityMapper.map(request);
        deliveryMissedReadRepository.save(deliveryMissedEntity);
        return DeliveryMissed.from(deliveryMissedEntity);
    }
}
