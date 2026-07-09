package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class DepotCodeRequestDto {
    private String depotCode;
    private ShipmentIdDto shipmentId;
    private ProcessTypeDto processType;

    public DepotCodeRequestDto() {
    }

    public DepotCodeRequestDto(final String depotCode, final ShipmentIdDto shipmentId, final ProcessTypeDto processType) {
        this.depotCode = depotCode;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }
}
