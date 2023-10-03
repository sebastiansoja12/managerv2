package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface SupplierTokenRequestMapper {

    SupplierTokenRequestDto map(SupplierTokenRequest supplierTokenRequest);
}
