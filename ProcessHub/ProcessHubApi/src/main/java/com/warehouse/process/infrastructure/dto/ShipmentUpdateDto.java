package com.warehouse.process.infrastructure.dto;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;

public record ShipmentUpdateDto(ShipmentIdDto shipmentId,
                                DeviceIdDto deviceId,
                                UserIdDto createdBy, DepartmentCodeDto departmentCode,
                                ServiceType serviceType,
                                String sourceService, String targetService,
                                String request, String response,
                                ProcessType processType) {
}
