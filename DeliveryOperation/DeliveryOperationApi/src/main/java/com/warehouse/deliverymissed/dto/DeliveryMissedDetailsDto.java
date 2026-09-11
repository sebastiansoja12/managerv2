package com.warehouse.deliverymissed.dto;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.SupplierCodeDto;

public record DeliveryMissedDetailsDto(TrackingNumber trackingNumber, DepartmentCodeDto departmentCode,
                                       SupplierCodeDto supplierCode, DeliveryStatusDto deliveryStatus) {
}
