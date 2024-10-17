package com.warehouse.reroute.domain.vo;

import lombok.Value;

@Value
public class RerouteTokenResponse {
    Integer token;
    ParcelId parcelId;
    boolean valid;
}
