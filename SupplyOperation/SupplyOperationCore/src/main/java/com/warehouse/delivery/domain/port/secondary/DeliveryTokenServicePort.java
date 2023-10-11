package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.model.DeliveryTokenRequest;
import com.warehouse.delivery.domain.model.DeliveryTokenResponse;

public interface DeliveryTokenServicePort {
    DeliveryTokenResponse protect(DeliveryTokenRequest request);
}
