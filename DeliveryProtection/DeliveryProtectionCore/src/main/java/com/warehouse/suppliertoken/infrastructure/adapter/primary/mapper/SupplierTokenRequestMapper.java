package com.warehouse.suppliertoken.infrastructure.adapter.primary.mapper;

import com.warehouse.suppliertoken.domain.model.*;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.DeliveryPackageRequestDto;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenRequestDto;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper
public interface SupplierTokenRequestMapper {

    default DeliveryPackageRequest map(DeliveryPackageRequestDto request) {
        return new DeliveryPackageRequest(new Parcel(request.getParcelId().getValue(), null, null, null),
                new Supplier(request.getSupplier().getSupplierCode()), new Delivery(request.getDelivery().getId()));
    }
    default SupplierTokenRequest map(SupplierTokenRequestDto supplierTokenRequest) {
        return new SupplierTokenRequest(supplierTokenRequest.getDeliveryPackageRequests()
                .stream()
                .map(this::map)
                .collect(Collectors.toList()));
    }
}
