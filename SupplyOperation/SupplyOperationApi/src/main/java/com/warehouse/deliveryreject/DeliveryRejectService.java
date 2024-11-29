package com.warehouse.deliveryreject;

import java.util.List;

import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;
import com.warehouse.deliveryreject.dto.response.DeliveryRejectResponseDto;

public interface DeliveryRejectService {
    List<DeliveryRejectResponseDto> reportDeliveryRejection(final List<DeliveryRejectRequestDto> deliveryRejectRequest);
}
