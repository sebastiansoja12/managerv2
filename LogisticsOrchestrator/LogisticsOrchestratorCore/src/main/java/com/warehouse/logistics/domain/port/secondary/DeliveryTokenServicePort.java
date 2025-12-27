package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.logistics.domain.vo.DeliveryTokenRequest;
import com.warehouse.logistics.domain.vo.DeliveryTokenResponse;

public interface DeliveryTokenServicePort {
    DeliveryTokenResponse protect(final DeliveryTokenRequest request);
}
