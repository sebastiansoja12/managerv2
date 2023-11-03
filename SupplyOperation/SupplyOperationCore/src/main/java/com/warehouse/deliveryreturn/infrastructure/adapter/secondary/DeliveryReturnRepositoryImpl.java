package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnRepository;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.entity.DeliveryReturnEntity;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryReturnMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryReturnRepositoryImpl implements DeliveryReturnRepository {

    private final DeliveryReturnReadRepository deliveryReturnReadRepository;

    private final DeliveryReturnMapper deliveryReturnMapper = getMapper(DeliveryReturnMapper.class);

    @Override
    public DeliveryReturn saveDeliveryReturn(DeliveryReturnDetails deliveryReturnRequest) {
        final DeliveryReturnEntity deliveryReturnEntity = deliveryReturnMapper.map(deliveryReturnRequest);
        deliveryReturnReadRepository.save(deliveryReturnEntity);
        return deliveryReturnMapper.map(deliveryReturnEntity);
    }
}
