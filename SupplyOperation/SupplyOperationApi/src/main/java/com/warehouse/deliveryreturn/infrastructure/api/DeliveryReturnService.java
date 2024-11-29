package com.warehouse.deliveryreturn.infrastructure.api;


import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnResponseDto;

public interface DeliveryReturnService {
    DeliveryReturnResponseDto processDeliveryReturn(final DeliveryReturnRequestDto deliveryReturnRequest);
}
