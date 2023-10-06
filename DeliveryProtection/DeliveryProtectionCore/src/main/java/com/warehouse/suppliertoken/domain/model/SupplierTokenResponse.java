package com.warehouse.suppliertoken.domain.model;

import lombok.Value;

import java.util.List;

@Value
public class SupplierTokenResponse {
    List<DeliveryPackageResponse> packageResponses;
}
