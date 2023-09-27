package com.warehouse.routetracker.infrastructure.adapter.primary.api.dto;

import lombok.Data;
import lombok.Value;

@Data
@Value
public class RouteResponseDto {
    RegisteredParcelDto registeredParcels;
    ResponsibleUserDto responsibleUser;
}
