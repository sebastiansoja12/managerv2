package com.warehouse.logistics.infrastructure.adapter.primary.process;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.SupplierCode;

public class DeliveryMissedDetails {
    private TrackingNumber trackingNumber;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private DeliveryStatus deliveryStatus;

    public DeliveryMissedDetails() {
    }

    public DeliveryMissedDetails(final TrackingNumber trackingNumber,
                                 final DepartmentCode departmentCode,
                                 final SupplierCode supplierCode,
                                 final DeliveryStatus deliveryStatus) {
        this.trackingNumber = trackingNumber;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(final TrackingNumber trackingNumber) {
        this.trackingNumber = trackingNumber;
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
}
