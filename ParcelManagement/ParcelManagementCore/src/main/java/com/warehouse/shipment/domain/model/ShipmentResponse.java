package com.warehouse.shipment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipmentResponse {

    String paymentUrl;
    Long parcelId;

}
