package com.warehouse.delivery.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DeliveryRequestDto {
    String username;
    Long parcelId;
    LocalDateTime created;
    String depotCode;
    String supplierCode;
}
