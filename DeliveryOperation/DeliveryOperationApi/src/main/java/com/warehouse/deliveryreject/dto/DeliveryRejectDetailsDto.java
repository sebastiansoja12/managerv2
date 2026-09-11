package com.warehouse.deliveryreject.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.commonassets.identificator.TrackingNumber;

public class DeliveryRejectDetailsDto {
    private final TrackingNumber trackingNumber;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final DeliveryStatusDto deliveryStatus;
    private final RejectReasonDto rejectReason;

    public DeliveryRejectDetailsDto(final TrackingNumber trackingNumber,
                                    final DepartmentCodeDto departmentCode,
                                    final SupplierCodeDto supplierCode,
                                    final DeliveryStatusDto deliveryStatus,
                                    final RejectReasonDto rejectReason) {
        this.trackingNumber = trackingNumber;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.rejectReason = rejectReason;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }

    public DeliveryStatusDto getDeliveryStatus() {
        return deliveryStatus;
    }

    public RejectReasonDto getRejectReason() {
        return rejectReason;
    }
}
