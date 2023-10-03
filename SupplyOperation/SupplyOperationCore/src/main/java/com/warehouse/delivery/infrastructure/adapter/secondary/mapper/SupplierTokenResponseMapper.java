package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenResponseDto;
import org.mapstruct.Mapper;

import com.warehouse.delivery.domain.model.SupplierTokenResponse;

@Mapper
public interface SupplierTokenResponseMapper {
    SupplierTokenResponse map(SupplierTokenResponseDto supplierTokenResponse);
}
