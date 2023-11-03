package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnTokenResponse;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import lombok.AllArgsConstructor;
import org.springframework.web.client.support.RestGatewaySupport;

@AllArgsConstructor
public class DeliveryReturnServiceAdapter extends RestGatewaySupport implements DeliveryReturnTokenServicePort {
    @Override
    public DeliveryReturnTokenResponse sign(DeliveryReturnTokenRequest deliveryReturnTokenRequest) {
        return null;
    }
}
