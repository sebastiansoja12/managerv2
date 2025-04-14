package com.warehouse.routelogger.dto;

public class DeliveryRequestDto {
    private DepartmentCodeDto departmentCode;
    private SupplierCodeDto supplierCode;
    private ShipmentIdDto shipmentId;
    private ProcessTypeDto processType;

    public DeliveryRequestDto() {
    }

	public DeliveryRequestDto(final DepartmentCodeDto departmentCode, final ProcessTypeDto processType,
			final ShipmentIdDto shipmentId, final SupplierCodeDto supplierCode) {
        this.departmentCode = departmentCode;
        this.processType = processType;
        this.shipmentId = shipmentId;
        this.supplierCode = supplierCode;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }
}
