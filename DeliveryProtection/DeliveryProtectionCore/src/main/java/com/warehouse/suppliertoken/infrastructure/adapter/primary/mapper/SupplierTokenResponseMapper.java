package com.warehouse.suppliertoken.infrastructure.adapter.primary.mapper;

import com.warehouse.suppliertoken.domain.model.SupplierTokenResponse;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface SupplierTokenResponseMapper {
    SupplierTokenResponseDto map(SupplierTokenResponse response);
}
