package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class UsernameRequestDto {
    private String username;
    private ShipmentIdDto shipmentId;
    private ProcessTypeDto processType;

    public UsernameRequestDto() {
    }

    public UsernameRequestDto(final String username, final ShipmentIdDto shipmentId, final ProcessTypeDto processType) {
        this.username = username;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getUsername() {
        return username;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }
}
