package com.warehouse.delivery.domain.model;


import com.warehouse.delivery.domain.enumeration.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryRequest {
    Long parcelId;
    String depotCode;
    String supplierCode;
    DeliveryStatus deliveryStatus;
}
