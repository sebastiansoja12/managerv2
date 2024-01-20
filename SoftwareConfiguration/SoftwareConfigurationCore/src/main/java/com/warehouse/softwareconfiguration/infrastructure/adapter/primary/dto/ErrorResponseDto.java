package com.warehouse.softwareconfiguration.infrastructure.adapter.primary.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorResponseDto {
    LocalDateTime timestamp;
    int status;
    String error;
}
