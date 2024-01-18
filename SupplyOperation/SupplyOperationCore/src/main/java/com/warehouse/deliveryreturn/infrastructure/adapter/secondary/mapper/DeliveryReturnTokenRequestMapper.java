package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryPackageRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnInformation;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ParcelReturnTokenRequestDto;
import org.mapstruct.Mapping;

@Mapper
public interface DeliveryReturnTokenRequestMapper {

    default List<ParcelReturnTokenRequestDto> map(DeliveryReturnTokenRequest deliveryReturnTokenRequest) {
        return deliveryReturnTokenRequest.getRequests().stream()
                .map(this::buildParcel)
                .collect(Collectors.toList());
    }

    default ParcelReturnTokenRequestDto buildParcel(DeliveryPackageRequest deliveryPackageRequest) {
        return Objects.isNull(deliveryPackageRequest) ? null : map(deliveryPackageRequest.getDelivery());
    }

    @Mapping(source = "deliveryStatus", target = "parcelStatus")
    @Mapping(source = "parcelId", target = "id")
    ParcelReturnTokenRequestDto map(DeliveryReturnInformation information);
}
