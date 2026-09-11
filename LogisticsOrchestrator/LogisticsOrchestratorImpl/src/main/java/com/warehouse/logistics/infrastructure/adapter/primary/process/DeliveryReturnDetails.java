package com.warehouse.logistics.infrastructure.adapter.primary.process;

import com.warehouse.logistics.domain.vo.ReturnToken;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.SupplierCode;

public class DeliveryReturnDetails {
    private TrackingNumber trackingNumber;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private DeliveryStatus deliveryStatus;
    private ReturnToken returnToken;

    public DeliveryReturnDetails(final TrackingNumber trackingNumber,
                                 final DepartmentCode departmentCode,
                                 final SupplierCode supplierCode,
                                 final DeliveryStatus deliveryStatus,
                                 final ReturnToken returnToken) {
        this.trackingNumber = trackingNumber;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
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

    public ReturnToken getReturnToken() {
        return returnToken;
    }

    public void setReturnToken(final ReturnToken returnToken) {
        this.returnToken = returnToken;
    }
}
