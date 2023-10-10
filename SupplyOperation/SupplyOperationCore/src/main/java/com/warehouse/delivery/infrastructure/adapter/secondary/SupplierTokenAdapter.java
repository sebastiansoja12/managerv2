package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.delivery.domain.model.SupplierTokenResponse;
import com.warehouse.delivery.domain.port.secondary.SupplierTokenServicePort;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryTokenRequestMapper;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryTokenResponseMapper;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.SupplierTokenService;

import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenRequestDto;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenResponseDto;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class SupplierTokenAdapter implements SupplierTokenServicePort {

    private final SupplierTokenService service;

    private final DeliveryTokenRequestMapper requestMapper = getMapper(DeliveryTokenRequestMapper.class);

    private final DeliveryTokenResponseMapper responseMapper = getMapper(DeliveryTokenResponseMapper.class);

    @Override
    public SupplierTokenResponse protect(SupplierTokenRequest supplierTokenRequest) {
        final SupplierTokenRequestDto request = requestMapper.map(supplierTokenRequest);
        final SupplierTokenResponseDto response = service.protect(request);
        return responseMapper.map(response);
    }
}
