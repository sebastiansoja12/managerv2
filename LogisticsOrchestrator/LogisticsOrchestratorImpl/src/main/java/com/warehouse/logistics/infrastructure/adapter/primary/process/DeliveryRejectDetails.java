package com.warehouse.logistics.infrastructure.adapter.primary.process;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreject.domain.vo.RejectReason;

public class DeliveryRejectDetails {
    private TrackingNumber trackingNumber;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private DeliveryStatus deliveryStatus;
    private RejectReason rejectReason;

    public DeliveryRejectDetails(final TrackingNumber trackingNumber,
                                 final DepartmentCode departmentCode,
                                 final SupplierCode supplierCode,
                                 final DeliveryStatus deliveryStatus,
                                 final RejectReason rejectReason) {
        this.trackingNumber = trackingNumber;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.rejectReason = rejectReason;
    }

    public void setTrackingNumber(final TrackingNumber trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setSupplierCode(final SupplierCode supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void setDeliveryStatus(final DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setRejectReason(final RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }
}
