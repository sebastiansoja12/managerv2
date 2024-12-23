package com.warehouse.returntoken.domain.model;

import java.util.UUID;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;

import lombok.Builder;

@Builder
public class ReturnPackageRequest {
    private final UUID id;
    private final ShipmentId shipmentId;
    private final DepartmentCode departmentCode;
    private final SupplierCode supplierCode;
    private final DeliveryStatus deliveryStatus;
    private final Boolean locked;

    public ReturnPackageRequest(final UUID id,
                                final ShipmentId shipmentId,
                                final DepartmentCode departmentCode,
                                final SupplierCode supplierCode,
                                final DeliveryStatus deliveryStatus,
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

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public Boolean isLocked() {
        return locked;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }
}
