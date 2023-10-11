package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.DeliveryTokenResponse;
import com.warehouse.delivery.domain.model.SupplierSignature;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.SupplierSignatureDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeliveryTokenResponseMapper {
    DeliveryTokenResponse map(DeliveryTokenResponseDto supplierTokenResponse);

    List<SupplierSignature> map(List<SupplierSignatureDto> supplierSignatureDtos);
}
