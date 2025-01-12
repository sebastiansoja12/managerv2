package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;

public class DeliveryRejectResponseDetails {
    private final RejectReasonId rejectReasonId;
    private final ShipmentId shipmentId;
    private final ShipmentId newShipmentId;
    private final SupplierCode supplierCode;
    private final DepartmentCode departmentCode;
    private final RejectReason rejectReason;
    private final DeliveryStatus deliveryStatus;

    public DeliveryRejectResponseDetails(final RejectReasonId rejectReasonId,
                                         final ShipmentId shipmentId,
                                         final ShipmentId newShipmentId,
                                         final SupplierCode supplierCode,
                                         final DepartmentCode departmentCode,
                                         final RejectReason rejectReason,
                                         final DeliveryStatus deliveryStatus) {
        this.rejectReasonId = rejectReasonId;
        this.shipmentId = shipmentId;
        this.newShipmentId = newShipmentId;
        this.supplierCode = supplierCode;
        this.departmentCode = departmentCode;
        this.rejectReason = rejectReason;
        this.deliveryStatus = deliveryStatus;
    }

    public RejectReasonId getRejectReasonId() {
        return rejectReasonId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ShipmentId getNewShipmentId() {
        return newShipmentId;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }
}
