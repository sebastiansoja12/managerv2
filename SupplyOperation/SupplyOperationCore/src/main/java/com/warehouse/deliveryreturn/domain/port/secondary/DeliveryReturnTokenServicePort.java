package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnTokenResponse;

public interface DeliveryReturnTokenServicePort {
    DeliveryReturnTokenResponse sign(DeliveryReturnTokenRequest deliveryReturnTokenRequest);
}
