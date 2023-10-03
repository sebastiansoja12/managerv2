package com.warehouse.suppliertoken.infrastructure.adapter.primary;


import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;
import com.warehouse.suppliertoken.domain.port.primary.SupplierTokenPort;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.SupplierTokenService;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenRequestDto;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenResponseDto;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.mapper.SupplierTokenRequestMapper;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.mapper.SupplierTokenResponseMapper;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class SupplierTokenAdapter implements SupplierTokenService {

    private final SupplierTokenRequestMapper requestMapper = getMapper(SupplierTokenRequestMapper.class);

    private final SupplierTokenResponseMapper responseMapper = getMapper(SupplierTokenResponseMapper.class);

    private final SupplierTokenPort supplierTokenPort;

    @Override
    public SupplierTokenResponseDto protect(SupplierTokenRequestDto supplierTokenRequest) {
        final SupplierTokenRequest request = requestMapper.map(supplierTokenRequest);
        final SupplierTokenResponse response = supplierTokenPort.protect(request);
        return responseMapper.map(response);
    }
}
