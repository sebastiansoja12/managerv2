package com.warehouse.delivery.infrastructure.adapter.primary.dto;

import java.time.LocalDateTime;


public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(final LocalDateTime timestamp, final int status, final String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
