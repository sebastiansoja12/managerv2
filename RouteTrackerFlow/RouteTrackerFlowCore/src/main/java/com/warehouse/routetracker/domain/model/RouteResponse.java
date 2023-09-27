package com.warehouse.routetracker.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
public class RouteResponse {

    RegisteredParcel registeredParcels;

    ResponsibleUser responsibleUser;
}
