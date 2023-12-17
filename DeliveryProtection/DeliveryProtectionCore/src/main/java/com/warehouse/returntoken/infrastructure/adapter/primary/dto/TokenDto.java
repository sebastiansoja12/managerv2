package com.warehouse.returntoken.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TokenDto {
    Long parcelId;
    String token;
}
