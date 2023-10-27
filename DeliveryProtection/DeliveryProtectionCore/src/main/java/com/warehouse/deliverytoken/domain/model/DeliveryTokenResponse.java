package com.warehouse.deliverytoken.domain.model;

import lombok.Value;

import java.util.List;

@Value
public class DeliveryTokenResponse {
    List<DeliveryPackageResponse> packageResponses;
}
