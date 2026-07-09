package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class SupplierCodeRequestDto {
    private String supplierCode;
    private ShipmentIdDto shipmentId;
    private ProcessTypeDto processType;

    public SupplierCodeRequestDto() {
    }

	public SupplierCodeRequestDto(final String supplierCode, final ShipmentIdDto shipmentId,
			final ProcessTypeDto processType) {
        this.supplierCode = supplierCode;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }
}
