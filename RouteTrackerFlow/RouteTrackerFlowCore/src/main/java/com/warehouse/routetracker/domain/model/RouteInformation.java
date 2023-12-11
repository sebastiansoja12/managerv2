package com.warehouse.routetracker.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
public class RouteInformation {

    UUID id;

    LocalDateTime created;

    Parcel parcel;

    User user;

    Supplier supplier;

    Depot depot;

    ParcelStatus parcelStatus;
}
