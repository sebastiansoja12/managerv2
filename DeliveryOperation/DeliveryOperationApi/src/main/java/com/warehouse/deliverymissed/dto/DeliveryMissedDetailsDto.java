package com.warehouse.deliverymissed.dto;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.DepartmentCodeDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.delivery.dto.SupplierCodeDto;

public record DeliveryMissedDetailsDto(ShipmentIdDto shipmentId, DepartmentCodeDto departmentCode,
                                       SupplierCodeDto supplierCode, DeliveryStatusDto deliveryStatus) {
}
