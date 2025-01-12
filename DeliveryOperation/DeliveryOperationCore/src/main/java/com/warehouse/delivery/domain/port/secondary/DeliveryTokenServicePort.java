package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.vo.DeliveryTokenRequest;
import com.warehouse.delivery.domain.vo.DeliveryTokenResponse;

public interface DeliveryTokenServicePort {
    DeliveryTokenResponse protect(final DeliveryTokenRequest request);
}
