package com.warehouse.route.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RouteDeleteRequest {
    UUID id;

    Long parcelId;

    String username;
}
