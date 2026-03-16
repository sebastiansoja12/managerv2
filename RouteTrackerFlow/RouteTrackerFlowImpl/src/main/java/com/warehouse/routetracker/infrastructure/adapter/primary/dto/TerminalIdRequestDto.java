package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class TerminalIdRequestDto {
    private ProcessTypeDto processType;
    private ShipmentIdDto shipmentId;
    private TerminalIdDto terminalId;

    public TerminalIdRequestDto() {
    }

	public TerminalIdRequestDto(final ProcessTypeDto processType, final ShipmentIdDto shipmentId,
			final TerminalIdDto terminalId) {
		this.processType = processType;
		this.shipmentId = shipmentId;
		this.terminalId = terminalId;
	}

    public ProcessTypeDto getProcessType() {
        return processType;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public TerminalIdDto getTerminalId() {
        return terminalId;
    }
}
