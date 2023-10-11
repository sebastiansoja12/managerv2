package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.enumeration.Status;
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
    Status deliveryStatus;
}
