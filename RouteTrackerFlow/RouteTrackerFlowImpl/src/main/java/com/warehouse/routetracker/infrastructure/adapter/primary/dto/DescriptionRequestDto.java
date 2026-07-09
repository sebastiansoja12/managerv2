package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class DescriptionRequestDto {
    private String value;
    private ShipmentIdDto shipmentId;
    private ProcessTypeDto processType;

    public DescriptionRequestDto() {
    }

    public DescriptionRequestDto(final String value, final ShipmentIdDto shipmentId, final ProcessTypeDto processType) {
        this.value = value;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getValue() {
        return value;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }
}
