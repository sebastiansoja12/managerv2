package com.warehouse.deliverytoken.infrastructure.adapter.primary.mapper;

import com.warehouse.deliverytoken.domain.model.*;
import com.warehouse.deliverytoken.domain.vo.Delivery;
import com.warehouse.deliverytoken.domain.vo.DeliveryPackageRequest;
import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.domain.vo.Supplier;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryPackageRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenRequestDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.SupplierDto;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper
public interface SupplierTokenRequestMapper {

    default DeliveryPackageRequest map(DeliveryPackageRequestDto request) {
        return new DeliveryPackageRequest(new Parcel(request.getParcelId().getValue(), null, null, null),
                 new Delivery(request.getDelivery().getId()));
    }
    default DeliveryTokenRequest map(DeliveryTokenRequestDto supplierTokenRequest) {
        return new DeliveryTokenRequest(supplierTokenRequest.getDeliveryPackageRequests()
                .stream()
                .map(this::map)
                .collect(Collectors.toList()), map(supplierTokenRequest.getSupplier()));
    }

    Supplier map(SupplierDto supplier);
}
