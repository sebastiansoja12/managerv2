package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.SupplierSignature;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.SupplierSignatureDto;
import org.mapstruct.Mapper;

import com.warehouse.delivery.domain.model.SupplierTokenResponse;

import java.util.List;

@Mapper
public interface DeliveryTokenResponseMapper {
    SupplierTokenResponse map(DeliveryTokenResponseDto supplierTokenResponse);

    List<SupplierSignature> map(List<SupplierSignatureDto> supplierSignatureDtos);
}
