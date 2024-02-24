package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RouteInformationDto {

    UUID id;

    LocalDateTime created;

    ParcelDto parcel;

    UserDto user;

    SupplierDto supplier;

    DepotDto depot;

    StatusDto status;
}
