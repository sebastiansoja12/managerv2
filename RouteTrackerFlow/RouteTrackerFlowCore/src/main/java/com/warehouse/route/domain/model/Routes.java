package com.warehouse.route.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Value
@Builder
public class Routes {

    UUID id;

    LocalDateTime created;

    Parcel parcel;

    User user;

    Supplier supplier;

    Depot depot;
}
