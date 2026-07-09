package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class ReturnTrackRequestDto {
    private ShipmentIdDto shipmentId;
    private ProcessTypeDto processType;
    private String username;
    private String depotCode;

    public ReturnTrackRequestDto() {
    }

	public ReturnTrackRequestDto(final ShipmentIdDto shipmentId, final ProcessTypeDto processType,
			final String username, final String depotCode) {
		this.shipmentId = shipmentId;
		this.processType = processType;
		this.username = username;
		this.depotCode = depotCode;
	}

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }

    public String getUsername() {
        return username;
    }

    public String getDepotCode() {
        return depotCode;
    }
}