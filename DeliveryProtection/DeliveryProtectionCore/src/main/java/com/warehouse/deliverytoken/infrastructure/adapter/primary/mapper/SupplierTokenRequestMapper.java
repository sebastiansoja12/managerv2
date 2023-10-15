package com.warehouse.deliverytoken.infrastructure.adapter.primary.mapper;

import com.warehouse.deliverytoken.domain.model.*;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryPackageRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenRequestDto;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper
public interface SupplierTokenRequestMapper {

    default DeliveryPackageRequest map(DeliveryPackageRequestDto request) {
        return new DeliveryPackageRequest(new Parcel(request.getParcelId().getValue(), null, null, null),
                new Supplier(request.getSupplier().getSupplierCode()), new Delivery(request.getDelivery().getId()));
    }
    default DeliveryTokenRequest map(DeliveryTokenRequestDto supplierTokenRequest) {
        return new DeliveryTokenRequest(supplierTokenRequest.getDeliveryPackageRequests()
                .stream()
                .map(this::map)
                .collect(Collectors.toList()));
    }
}