package com.warehouse.logistics.infrastructure.adapter.secondary.mapper;

import com.warehouse.logistics.domain.vo.DeliveryTokenResponse;
import com.warehouse.logistics.domain.vo.SupplierSignature;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.SupplierSignatureDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeliveryTokenResponseMapper {
    DeliveryTokenResponse map(DeliveryTokenResponseDto supplierTokenResponse);

    List<SupplierSignature> map(List<SupplierSignatureDto> supplierSignatureDtos);
}
