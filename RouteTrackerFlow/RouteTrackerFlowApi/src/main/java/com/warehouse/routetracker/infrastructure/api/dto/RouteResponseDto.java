package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Data;
import lombok.Value;

import java.util.UUID;

@Data
@Value
public class RouteResponseDto {
    UUID id;
}
