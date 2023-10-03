package com.warehouse.suppliertoken.infrastructure.adapter.primary.api;

import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenRequestDto;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenResponseDto;

public interface SupplierTokenService {
    SupplierTokenResponseDto protect(SupplierTokenRequestDto request);
}
