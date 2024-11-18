package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class TerminalVersionRequestDto {
    private String version;
    private ShipmentIdDto shipmentId;
    private ProcessTypeDto processType;

    public TerminalVersionRequestDto() {
    }

	public TerminalVersionRequestDto(final ShipmentIdDto shipmentId, final String version,
			final ProcessTypeDto processType) {
        this.shipmentId = shipmentId;
        this.version = version;
        this.processType = processType;
    }

    public String getVersion() {
        return version;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }
}

