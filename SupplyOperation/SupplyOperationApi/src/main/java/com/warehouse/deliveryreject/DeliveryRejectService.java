package com.warehouse.deliveryreject;

import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;

public interface DeliveryRejectService {
    DeliveryRejectResponseDto processDeliveryReject(final DeliveryRejectRequestDto deliveryRejectRequest);
}
