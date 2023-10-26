package com.warehouse.exceptionhandler.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
}
