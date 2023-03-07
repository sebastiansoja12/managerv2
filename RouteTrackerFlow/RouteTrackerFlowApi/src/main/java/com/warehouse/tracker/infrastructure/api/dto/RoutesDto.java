package com.warehouse.tracker.infrastructure.api.dto;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class RoutesDto {

    UUID id;

    LocalDateTime created;

    ParcelDto parcel;

    UserDto user;

    SupplierDto supplier;

    DepotDto depot;

    public LocalDateTime getCreated() {
        return created;
    }

}
