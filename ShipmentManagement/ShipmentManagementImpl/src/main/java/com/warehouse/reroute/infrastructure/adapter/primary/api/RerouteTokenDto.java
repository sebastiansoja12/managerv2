package com.warehouse.reroute.infrastructure.adapter.primary.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RerouteTokenDto {
    Long parcelId;
    String email;
}
