package com.warehouse.delivery.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DeliveryRequestDto {
    UUID id;
    String username;
    Long parcelId;
    LocalDateTime created;
    String depotCode;
    String supplierCode;
}
