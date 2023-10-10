package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.SupplierSignature;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierSignatureDto;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenResponseDto;
import org.mapstruct.Mapper;

import com.warehouse.delivery.domain.model.SupplierTokenResponse;

import java.util.List;

@Mapper
public interface DeliveryTokenResponseMapper {
    SupplierTokenResponse map(SupplierTokenResponseDto supplierTokenResponse);

    List<SupplierSignature> map(List<SupplierSignatureDto> supplierSignatureDtos);
}
