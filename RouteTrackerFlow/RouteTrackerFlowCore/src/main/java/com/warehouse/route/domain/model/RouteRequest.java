package com.warehouse.route.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {

    UUID id;
    Long parcelId;
    Long userId;
    Long supplierId;
    String depotCode;
}
