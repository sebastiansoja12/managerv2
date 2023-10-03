package com.warehouse.suppliertoken.infrastructure.adapter.primary.mapper;

import com.warehouse.suppliertoken.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface SupplierTokenRequestMapper {
    SupplierTokenRequest map(SupplierTokenRequestDto supplierTokenRequest);
}
