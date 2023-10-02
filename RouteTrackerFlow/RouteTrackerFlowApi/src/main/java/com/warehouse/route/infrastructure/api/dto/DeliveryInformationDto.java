package com.warehouse.route.infrastructure.api.dto;

import lombok.Data;

@Data
public class DeliveryInformationDto {

    Long parcelId;
    String depotCode;
    String supplierCode;
    String deliveryStatus;
    String token;

}
