package com.warehouse.delivery.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeliveryTokenRequest {
    private List<DeliveryPackageRequest> deliveryPackageRequests;
    private Supplier supplier;
}
