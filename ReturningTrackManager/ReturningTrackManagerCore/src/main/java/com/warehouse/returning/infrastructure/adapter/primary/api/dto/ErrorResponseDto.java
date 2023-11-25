package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ErrorResponseDto {
    LocalDateTime timestamp;
    int status;
    String error;
}
