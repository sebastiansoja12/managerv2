package com.warehouse.reroute.infrastructure.api.dto;


import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class EmailDto {
    @NonNull
    String value;
}
