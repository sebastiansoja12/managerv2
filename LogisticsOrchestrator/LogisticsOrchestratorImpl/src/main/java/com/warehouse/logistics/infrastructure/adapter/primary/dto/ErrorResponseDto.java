package com.warehouse.logistics.infrastructure.adapter.primary.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ErrorResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponseDto {

    @XmlElement(name = "Timestamp")
    private String timestamp;
    @XmlElement(name = "Status")
    private int status;
    @XmlElement(name = "Error")
    private String error;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(final String timestamp, final int status, final String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
