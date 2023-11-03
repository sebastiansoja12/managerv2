package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.vo.DeliveryTokenRequest;
import com.warehouse.delivery.domain.vo.DeliveryTokenResponse;
import com.warehouse.delivery.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryTokenRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryTokenResponseMapper;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;

import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class DeliveryTokenAdapter implements DeliveryTokenServicePort {

    private final DeliveryTokenService service;

    private final DeliveryTokenRequestMapper requestMapper = getMapper(DeliveryTokenRequestMapper.class);

    private final DeliveryTokenResponseMapper responseMapper = getMapper(DeliveryTokenResponseMapper.class);

    @Override
    public DeliveryTokenResponse protect(DeliveryTokenRequest deliveryTokenRequest) {
        final DeliveryTokenRequestDto request = requestMapper.map(deliveryTokenRequest);
        final DeliveryTokenResponseDto response = service.protect(request);
        return responseMapper.map(response);
    }
}
