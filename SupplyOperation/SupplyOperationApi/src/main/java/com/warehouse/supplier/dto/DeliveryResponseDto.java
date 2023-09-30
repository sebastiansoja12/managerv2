package com.warehouse.supplier.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DeliveryResponseDto {
    UUID id;
    Long parcelId;
    String deliveryStatus;
}
