package com.warehouse.returning.infrastructure.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;
}
