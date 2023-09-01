package com.warehouse.route.infrastructure.api.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class SupplyInformationDto {

    UUID id;
    String username;
    Long parcelId;
    String depotCode;
    String supplierCode;
    String deliveryStatus;
    String token;

}
