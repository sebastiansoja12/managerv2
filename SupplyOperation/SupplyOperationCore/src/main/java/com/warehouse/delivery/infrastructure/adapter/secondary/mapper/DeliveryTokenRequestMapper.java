package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.DeliveryPackageRequest;
import com.warehouse.delivery.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryPackageRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeliveryTokenRequestMapper {

    DeliveryTokenRequestDto map(DeliveryTokenRequest deliveryTokenRequest);

    @Mapping(target = "parcelId.value", source = "parcelId")
    DeliveryPackageRequestDto map(DeliveryPackageRequest deliveryPackageRequest);
}
