package com.warehouse.reroute.infrastructure.api.dto;

import lombok.Data;

@Data
public class RerouteRequestDto {
    ParcelId parcelId;
    EmailDto email;
}
