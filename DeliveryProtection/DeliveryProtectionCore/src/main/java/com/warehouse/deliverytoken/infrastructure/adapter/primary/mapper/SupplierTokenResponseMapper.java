package com.warehouse.deliverytoken.infrastructure.adapter.primary.mapper;

import com.warehouse.deliverytoken.domain.vo.DeliveryPackageResponse;
import com.warehouse.deliverytoken.domain.vo.DeliveryTokenResponse;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.SupplierSignatureDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SupplierTokenResponseMapper {
    
    @Mapping(target = "supplierSignature", source = "packageResponses", qualifiedByName = "mapToSupplierSignature")
    DeliveryTokenResponseDto map(DeliveryTokenResponse response);

    
    @Named("mapToSupplierSignature")
    default List<SupplierSignatureDto> map(List<DeliveryPackageResponse> deliveryPackageResponses) {
		return deliveryPackageResponses.stream()
				.map(this::map)
				.collect(Collectors.toList());
    }

    @Mapping(target = "deliveryId", source = "protectedDelivery.deliveryId")
    @Mapping(target = "token", source = "protectedDelivery.protectionToken")
    SupplierSignatureDto map(DeliveryPackageResponse deliveryPackageResponse);
}
