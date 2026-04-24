package com.warehouse.process.infrastructure.dto;

public record InitializeProcessRequestDto(
        DeviceInformationDto deviceInformation,
        String request
) {
}
