package com.warehouse.deliveryreject.dto.request;

import com.warehouse.deliveryreject.dto.*;

public record DeliveryRejectRequestDto(DeviceIdDto deviceId, DepartmentCodeDto departmentCode, SupplierCodeDto supplierCode,
                                       DeliveryStatusDto deliveryStatus, ShipmentIdDto shipmentId,
                                       RejectReasonDto rejectReason) {
}
