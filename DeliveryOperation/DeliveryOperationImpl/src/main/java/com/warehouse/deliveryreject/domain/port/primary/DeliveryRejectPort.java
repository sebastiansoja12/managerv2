package com.warehouse.deliveryreject.domain.port.primary;

import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;

public interface DeliveryRejectPort {
    DeliveryRejectResponse deliverReject(final DeliveryRejectRequest request);
}
