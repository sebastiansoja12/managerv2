package com.warehouse.shipment.domain.vo;

import lombok.Value;

@Value
public class ShipmentResponse {
    String routeProcessId;
    Long parcelId;

}
