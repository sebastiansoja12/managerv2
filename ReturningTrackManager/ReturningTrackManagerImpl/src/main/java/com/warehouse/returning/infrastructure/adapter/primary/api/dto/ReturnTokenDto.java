package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ReturnTokenDto {
    String value;
}
