package com.warehouse.shipment.infrastructure.api.dto;

import lombok.Data;

@Data
public class ShipmentResponseDto {
    String paymentUrl;
    Long parcelId;
}
