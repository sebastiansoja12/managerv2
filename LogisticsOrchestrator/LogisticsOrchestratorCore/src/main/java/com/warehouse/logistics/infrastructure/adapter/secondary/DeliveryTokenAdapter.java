package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.logistics.domain.vo.DeliveryTokenRequest;
import com.warehouse.logistics.domain.vo.DeliveryTokenResponse;
import com.warehouse.logistics.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryTokenRequestMapper;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryTokenResponseMapper;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;

import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class DeliveryTokenAdapter implements DeliveryTokenServicePort {

    private final DeliveryTokenService deliveryTokenService;

    private final DeliveryTokenRequestMapper requestMapper = getMapper(DeliveryTokenRequestMapper.class);

    private final DeliveryTokenResponseMapper responseMapper = getMapper(DeliveryTokenResponseMapper.class);

    @Override
    public DeliveryTokenResponse protect(final DeliveryTokenRequest deliveryTokenRequest) {
        final DeliveryTokenRequestDto request = requestMapper.map(deliveryTokenRequest);
        final DeliveryTokenResponseDto response = deliveryTokenService.signDelivery(request);
        return responseMapper.map(response);
    }
}
