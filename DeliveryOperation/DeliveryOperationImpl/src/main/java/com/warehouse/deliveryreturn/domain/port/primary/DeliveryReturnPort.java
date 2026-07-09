package com.warehouse.deliveryreturn.domain.port.primary;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;

public interface DeliveryReturnPort {
    DeliveryReturnResponse deliverReturn(final DeliveryReturnRequest deliveryRequest);
}
