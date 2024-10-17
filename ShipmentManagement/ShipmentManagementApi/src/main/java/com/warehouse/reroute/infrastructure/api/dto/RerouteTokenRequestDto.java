package com.warehouse.reroute.infrastructure.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RerouteTokenRequestDto {

    Long parcelId;

    String email;
}
