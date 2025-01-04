package com.warehouse.deliveryreject;

import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;

public interface DeliveryRejectService {
    DeliveryRejectResponseDto deliverReject(final DeliveryRejectRequestDto deliveryRejectRequest);
}
