package com.warehouse.deliverytoken.infrastructure.adapter.primary;


import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.domain.vo.DeliveryTokenResponse;
import com.warehouse.deliverytoken.domain.port.primary.DeliveryTokenPort;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.mapper.SupplierTokenRequestMapper;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.mapper.SupplierTokenResponseMapper;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class DeliveryTokenAdapter implements DeliveryTokenService {

    private final SupplierTokenRequestMapper requestMapper = getMapper(SupplierTokenRequestMapper.class);

    private final SupplierTokenResponseMapper responseMapper = getMapper(SupplierTokenResponseMapper.class);

    private final DeliveryTokenPort deliveryTokenPort;

    @Override
    public DeliveryTokenResponseDto signDelivery(final DeliveryTokenRequestDto deliveryTokenRequest) {
        final DeliveryTokenRequest request = requestMapper.map(deliveryTokenRequest);
        final DeliveryTokenResponse response = deliveryTokenPort.protect(request);
        return responseMapper.map(response);
    }
}
