package com.warehouse.redirect.infrastructure.adapter.primary.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TokenDto {
    String value;
}
