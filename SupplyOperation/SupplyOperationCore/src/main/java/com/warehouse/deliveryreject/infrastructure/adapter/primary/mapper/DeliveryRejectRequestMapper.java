package com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper;

import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryRejectRequestMapper {
    DeliveryRejectRequest map(final DeliveryRejectRequestDto deliveryRejectRequest);
}
