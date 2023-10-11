package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.supplier.dto.DeliveryResponseDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper
public interface DeliveryResponseMapper {
    DeliveryResponseDto map(DeliveryResponse deliveryResponse);

    List<DeliveryResponseDto> map(List<DeliveryResponse> responses);
}
