package com.warehouse.deliverytoken.infrastructure.adapter.secondary;

import com.warehouse.deliverytoken.domain.model.DeliveryTokenResponse;
import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.property.STSProperty;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.support.RestGatewaySupport;

import java.util.Collections;


@AllArgsConstructor
public class DeliveryTokenAdapter extends RestGatewaySupport implements DeliveryTokenServicePort {

    private final STSProperty stsProperty;

    @Override
    public DeliveryTokenResponse protect(DeliveryTokenRequest request) {
        // TODO INPL-6151
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Stage", stsProperty.getStage());
        return new DeliveryTokenResponse(Collections.emptyList());
    }
}