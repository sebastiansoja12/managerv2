package com.warehouse.deliverytoken.domain.vo;

import java.util.List;

import lombok.Value;

@Value
public class DeliveryTokenResponse {
    List<DeliveryPackageResponse> packageResponses;
    String supplierCode;
}
