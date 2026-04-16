package com.warehouse.logistics.domain.vo;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryPackageRequest {
    DeliveryInformation delivery;
}
