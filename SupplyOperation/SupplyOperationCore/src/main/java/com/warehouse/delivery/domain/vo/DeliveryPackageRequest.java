package com.warehouse.delivery.domain.vo;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryPackageRequest {
    DeliveryInformation delivery;
}
