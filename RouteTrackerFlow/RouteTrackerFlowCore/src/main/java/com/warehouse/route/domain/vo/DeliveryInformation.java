package com.warehouse.route.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryInformation {
    Long parcelId;
    String depotCode;
    String supplierCode;
    String token;
    String deliveryStatus;
}
