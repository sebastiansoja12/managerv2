package com.warehouse.deliverycollect.api;

import com.warehouse.deliverycollect.api.dto.DeliveryCollectRequestDto;
import com.warehouse.deliverycollect.api.dto.DeliveryCollectResponseDto;

public interface DeliveryCollectApiService {
    DeliveryCollectResponseDto collect(final DeliveryCollectRequestDto deliveryCollectRequest);
}
