package com.warehouse.shipment.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class RouteProcess {
    Long parcelId;
    UUID processId;
}
