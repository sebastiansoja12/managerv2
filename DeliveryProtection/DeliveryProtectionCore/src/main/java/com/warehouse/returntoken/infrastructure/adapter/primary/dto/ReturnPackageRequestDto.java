package com.warehouse.returntoken.infrastructure.adapter.primary.dto;

public class ReturnPackageRequestDto {
    private ShipmentIdDto shipmentId;
    private DepartmentCodeDto departmentCode;
    private SupplierCodeDto supplierCode;
    private String deliveryStatus;
    private Boolean locked;

    public ReturnPackageRequestDto(final ShipmentIdDto shipmentId,
                                   final DepartmentCodeDto departmentCode,
                                   final SupplierCodeDto supplierCode,
                                   final String deliveryStatus,
                                   final Boolean locked) {
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.locked = locked;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public Boolean getLocked() {
        return locked;
    }
}
