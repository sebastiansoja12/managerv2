package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.DeliveryPackageRequest;
import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.DeliveryPackageRequestDto;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.dto.SupplierTokenRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeliveryTokenRequestMapper {

    SupplierTokenRequestDto map(SupplierTokenRequest supplierTokenRequest);

    @Mapping(target = "parcelId.value", source = "parcelId")
    DeliveryPackageRequestDto map(DeliveryPackageRequest deliveryPackageRequest);
}
