package com.warehouse.deliveryreturn.domain.vo;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryPackageRequest {
    DeliveryReturnInformation delivery;
}
