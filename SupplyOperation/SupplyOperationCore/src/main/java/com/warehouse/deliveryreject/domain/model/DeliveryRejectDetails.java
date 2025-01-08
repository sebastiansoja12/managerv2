package com.warehouse.deliveryreject.domain.model;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreturn.domain.exception.WrongDeliveryStatusException;

public class DeliveryRejectDetails {
    private ShipmentId shipmentId;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private DeliveryStatus deliveryStatus;
    private RejectReason rejectReason;
    private ShipmentStatus shipmentStatus;
    private ProcessType processType;

    public DeliveryRejectDetails(final ShipmentId shipmentId,
                                 final DepartmentCode departmentCode,
                                 final SupplierCode supplierCode,
                                 final DeliveryStatus deliveryStatus,
                                 final RejectReason rejectReason,
                                 final ShipmentStatus shipmentStatus,
                                 final ProcessType processType) {
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.rejectReason = rejectReason;
        this.shipmentStatus = shipmentStatus;
        this.processType = processType;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(final SupplierCode supplierCode) {
        this.supplierCode = supplierCode;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(final DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(final RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public void validateDeliveryStatus() {
        if (!DeliveryStatus.REJECTED.equals(deliveryStatus)) {
            throw new WrongDeliveryStatusException(500, "Wrong delivery status");
        }
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }
}
