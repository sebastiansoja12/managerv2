package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import com.warehouse.routelogger.dto.ProcessTypeDto;
import com.warehouse.routelogger.dto.SupplierCodeDto;


public record DeliveryRequestDto(DepartmentCodeDto departmentCode,
                                 SupplierCodeDto supplierCode,
                                 ShipmentIdDto shipmentId,
                                 ProcessTypeDto processType) {
}
