package com.warehouse.deliveryreject.domain.port.primary;

import java.util.List;

import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;

public interface DeliveryRejectPort {
    List<DeliveryRejectResponse> registerDeliveryRejection(final List<DeliveryRejectRequest> request);
}
