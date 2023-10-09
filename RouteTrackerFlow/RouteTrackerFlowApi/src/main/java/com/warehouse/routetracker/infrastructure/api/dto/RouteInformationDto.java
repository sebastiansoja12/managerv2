package com.warehouse.routetracker.infrastructure.api.dto;

import com.warehouse.tracker.infrastructure.api.dto.DepotDto;
import com.warehouse.tracker.infrastructure.api.dto.ParcelDto;
import com.warehouse.tracker.infrastructure.api.dto.SupplierDto;
import com.warehouse.tracker.infrastructure.api.dto.UserDto;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class RouteInformationDto {

    UUID id;

    LocalDateTime created;

    ParcelDto parcel;

    UserDto user;

    SupplierDto supplier;

    DepotDto depot;

    StatusDto status;
}
