package com.warehouse.deliverytoken.infrastructure.adapter.secondary;

import com.warehouse.deliverytoken.domain.model.DeliveryTokenResponse;
import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import lombok.AllArgsConstructor;
import org.springframework.web.client.support.RestGatewaySupport;


@AllArgsConstructor
public class DeliveryTokenAdapter extends RestGatewaySupport implements DeliveryTokenServicePort {

    @Override
    public DeliveryTokenResponse protect(DeliveryTokenRequest request) {
        // TODO INPL-6151
        return null;
    }
}