package com.warehouse.logistics.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;
import com.warehouse.logistics.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.logistics.domain.vo.DeliveryTokenRequest;
import com.warehouse.logistics.domain.vo.DeliveryTokenResponse;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryTokenRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryTokenResponseMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryTokenAdapter implements DeliveryTokenServicePort {

    private final DeliveryTokenService deliveryTokenService;

    private final DeliveryTokenRequestMapper requestMapper = getMapper(DeliveryTokenRequestMapper.class);

    private final DeliveryTokenResponseMapper responseMapper = getMapper(DeliveryTokenResponseMapper.class);

    // TODO
    @Override
    public DeliveryTokenResponse protect(final DeliveryTokenRequest deliveryTokenRequest) {
        return null;
    }
}
