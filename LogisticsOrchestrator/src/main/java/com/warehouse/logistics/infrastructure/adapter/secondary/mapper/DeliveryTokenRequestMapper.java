package com.warehouse.logistics.infrastructure.adapter.secondary.mapper;

import com.warehouse.logistics.domain.vo.DeliveryPackageRequest;
import com.warehouse.logistics.domain.vo.DeliveryTokenRequest;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryPackageRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeliveryTokenRequestMapper {

    DeliveryTokenRequestDto map(DeliveryTokenRequest deliveryTokenRequest);

    @Mapping(target = "parcelId.value", source = "delivery.parcelId")
    DeliveryPackageRequestDto map(DeliveryPackageRequest deliveryPackageRequest);
}
