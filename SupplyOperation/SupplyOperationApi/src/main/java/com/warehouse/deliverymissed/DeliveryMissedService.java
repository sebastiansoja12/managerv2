package com.warehouse.deliverymissed;

import com.warehouse.deliverymissed.dto.DeliveryMissedRequestDto;
import com.warehouse.deliverymissed.dto.DeliveryMissedResponseDto;

public interface DeliveryMissedService {
    DeliveryMissedResponseDto processDeliveryMiss(final DeliveryMissedRequestDto deliveryMissedRequestDto);
}
