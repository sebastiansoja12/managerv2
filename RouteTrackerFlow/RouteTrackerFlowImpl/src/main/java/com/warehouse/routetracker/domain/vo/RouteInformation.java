package com.warehouse.routetracker.domain.vo;

import java.time.LocalDateTime;
import java.util.UUID;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.model.Depot;
import com.warehouse.routetracker.domain.model.Parcel;
import com.warehouse.routetracker.domain.model.Supplier;
import lombok.Builder;
import lombok.Value;

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
