package com.warehouse.deliverymissed.domain.vo;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;

import lombok.Builder;

@Builder
public final class DeliveryMissedInformation {
    private final ShipmentId shipmentId;
    private final SupplierCode supplierCode;
    private final DepartmentCode departmentCode;
    private final DeliveryStatus deliveryStatus;

    public DeliveryMissedInformation(final ShipmentId shipmentId,
                                     final SupplierCode supplierCode,
                                     final DepartmentCode departmentCode,
                                     final DeliveryStatus deliveryStatus) {
        this.shipmentId = shipmentId;
        this.supplierCode = supplierCode;
        this.departmentCode = departmentCode;
        this.deliveryStatus = deliveryStatus;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }
}
