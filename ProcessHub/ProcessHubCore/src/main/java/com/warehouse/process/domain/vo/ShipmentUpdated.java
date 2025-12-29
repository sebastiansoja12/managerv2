package com.warehouse.process.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

public record ShipmentUpdated(ShipmentId shipmentId, DeviceId deviceId,
                              UserId createdBy, DepartmentCode departmentCode, ServiceType serviceType,
                              ProcessType processType, String sourceService, String targetService,
                              String request, String response) {
}
