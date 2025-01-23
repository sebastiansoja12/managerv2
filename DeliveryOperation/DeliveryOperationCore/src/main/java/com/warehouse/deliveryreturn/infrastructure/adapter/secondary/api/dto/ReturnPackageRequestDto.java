package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

import java.util.UUID;

public class ReturnPackageRequestDto {
    private UUID id;
    private ShipmentIdDto shipmentId;
    private DepartmentCodeDto departmentCode;
    private SupplierCodeDto supplierCode;
    private String deliveryStatus;
    private Boolean locked;

    public ReturnPackageRequestDto() {
    }

    public ReturnPackageRequestDto(final UUID id,
                                   final ShipmentIdDto shipmentId,
                                   final DepartmentCodeDto departmentCode,
                                   final SupplierCodeDto supplierCode,
                                   final String deliveryStatus,
                                   final Boolean locked) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.locked = locked;
    }

    public UUID getId() {
        return id;
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
