package com.warehouse.reroute.domain.vo;

import lombok.Value;

@Value
public class RerouteProcessor {
    String email;
    Long parcelId;
    GeneratedToken generatedToken;
}
