package com.warehouse.deliverytoken.infrastructure.adapter.secondary;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.deliverytoken.domain.vo.DeliveryTokenResponse;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DeliveryTokenAdapter extends RestGatewaySupport implements DeliveryTokenServicePort {


    @Override
    public DeliveryTokenResponse protect(DeliveryTokenRequest request) {
        // TODO INPL-6151
        final HttpHeaders httpHeaders = new HttpHeaders();
        return new DeliveryTokenResponse(Collections.emptyList(), request.extractSupplierCode());
    }
}