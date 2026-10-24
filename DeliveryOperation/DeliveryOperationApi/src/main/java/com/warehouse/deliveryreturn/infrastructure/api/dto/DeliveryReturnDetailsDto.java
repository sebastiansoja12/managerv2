package com.warehouse.deliveryreturn.infrastructure.api.dto;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.SupplierCodeDto;

public class DeliveryReturnDetailsDto {
    private final TrackingNumber trackingNumber;
    private final DeliveryStatusDto deliveryStatus;
    private final DepartmentCodeDto departmentCode;
    private final SupplierCodeDto supplierCode;
    private final ReturnTokenDto returnToken;

    public DeliveryReturnDetailsDto(final TrackingNumber trackingNumber,
                                    final DeliveryStatusDto deliveryStatus,
                                    final DepartmentCodeDto departmentCode,
                                    final SupplierCodeDto supplierCode,
                                    final ReturnTokenDto returnToken) {
        this.trackingNumber = trackingNumber;
        this.deliveryStatus = deliveryStatus;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.returnToken = returnToken;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public DeliveryStatusDto getDeliveryStatus() {
        return deliveryStatus;
    }

    public DepartmentCodeDto getDepartmentCode() {
        return departmentCode;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }

    public ReturnTokenDto getReturnToken() {
        return returnToken;
    }

}
