package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.logistics.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.logistics.domain.vo.DeliveryTokenRequest;
import com.warehouse.logistics.domain.vo.DeliveryTokenResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryTokenAdapter implements DeliveryTokenServicePort {

    // TODO
    @Override
    public DeliveryTokenResponse protect(final DeliveryTokenRequest deliveryTokenRequest) {
        return null;
    }
}
