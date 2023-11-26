package com.warehouse.redirect.infrastructure.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RedirectResponseDto {
    TokenDto token;
    Long parcelId;
}
