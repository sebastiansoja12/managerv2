package com.warehouse.deliverytoken.domain.port.secondary;

import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.domain.model.DeliveryTokenResponse;

public interface DeliveryTokenServicePort {
    DeliveryTokenResponse protect(DeliveryTokenRequest request);
}
