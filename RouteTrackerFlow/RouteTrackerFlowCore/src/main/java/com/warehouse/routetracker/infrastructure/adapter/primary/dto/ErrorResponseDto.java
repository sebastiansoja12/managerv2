package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import java.time.LocalDateTime;


public class ErrorResponseDto {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;

    public ErrorResponseDto(LocalDateTime timestamp, int status, String error) {
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
