package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class TerminalRequestDto {
    private ProcessTypeDto processType;
    private ShipmentIdDto shipmentId;
    private String request;

    public TerminalRequestDto() {
    }

    public TerminalRequestDto(final ProcessTypeDto processType, final ShipmentIdDto shipmentId, final String request) {
        this.processType = processType;
        this.shipmentId = shipmentId;
        this.request = request;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public String getRequest() {
        return request;
    }
}
