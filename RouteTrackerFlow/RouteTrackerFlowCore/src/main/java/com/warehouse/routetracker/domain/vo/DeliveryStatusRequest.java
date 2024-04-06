package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.enumeration.ProcessType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryStatusRequest {
    Long parcelId;
    String supplierCode;
    String depotCode;
    ProcessType processType;
}
