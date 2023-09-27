package com.warehouse.routetracker.domain.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {

    List<Long> parcelIdList;
    String username;
    String supplierCode;
    String depotCode;
}
