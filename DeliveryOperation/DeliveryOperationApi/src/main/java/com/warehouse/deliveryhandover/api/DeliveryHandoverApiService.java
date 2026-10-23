package com.warehouse.deliveryhandover.api;

import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverRequestDto;
import com.warehouse.deliveryhandover.api.dto.DeliveryHandoverResponseDto;

public interface DeliveryHandoverApiService {
    DeliveryHandoverResponseDto handOver(final DeliveryHandoverRequestDto deliveryHandoverRequest);
}
