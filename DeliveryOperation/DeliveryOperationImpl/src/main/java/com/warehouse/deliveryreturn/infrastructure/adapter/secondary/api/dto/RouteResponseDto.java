package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class RouteResponseDto {
    Long parcelId;
    UUID id;
}
