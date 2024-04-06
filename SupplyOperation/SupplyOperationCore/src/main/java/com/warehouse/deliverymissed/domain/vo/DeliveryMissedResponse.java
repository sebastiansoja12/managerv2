package com.warehouse.deliverymissed.domain.vo;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class DeliveryMissedResponse {
    String deliveryId;
    Long parcelId;
    String depotCode;
    String supplierCode;
    LocalDateTime timestamp;
}
