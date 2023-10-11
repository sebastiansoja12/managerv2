package com.warehouse.delivery.dto;

import lombok.Data;

@Data
public class DeliveryRequestDto {
    Long parcelId;
    String depotCode;
    String supplierCode;
}
