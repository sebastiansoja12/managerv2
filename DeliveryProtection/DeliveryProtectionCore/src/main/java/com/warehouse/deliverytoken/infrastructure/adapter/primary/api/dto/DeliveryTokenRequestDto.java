package com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto;

import lombok.Value;

import java.util.List;

@Value
public class DeliveryTokenRequestDto {
    List<DeliveryPackageRequestDto> deliveryPackageRequests;
    SupplierDto supplier;
}
