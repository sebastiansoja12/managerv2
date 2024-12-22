package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnInformation;
import com.warehouse.deliveryreturn.domain.vo.ReturnPackageRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentReturnTokenRequestDto;

@Mapper
public interface DeliveryReturnTokenRequestMapper {

    default List<ShipmentReturnTokenRequestDto> map(ReturnTokenRequest returnTokenRequest) {
        return returnTokenRequest.getReturnPackageRequests().stream()
                .map(this::buildParcel)
                .collect(Collectors.toList());
    }

    default ShipmentReturnTokenRequestDto buildParcel(ReturnPackageRequest returnPackageRequest) {
        return null;
    }

    @Mapping(source = "deliveryStatus", target = "parcelStatus")
    @Mapping(source = "shipmentId", target = "id")
    ShipmentReturnTokenRequestDto map(final DeliveryReturnInformation information);
}
