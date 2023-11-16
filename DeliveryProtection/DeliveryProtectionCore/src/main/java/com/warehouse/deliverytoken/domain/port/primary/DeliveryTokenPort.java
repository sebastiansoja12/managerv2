package com.warehouse.deliverytoken.domain.port.primary;

import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.domain.vo.DeliveryTokenResponse;

public interface DeliveryTokenPort {
    DeliveryTokenResponse protect(DeliveryTokenRequest request);
}
