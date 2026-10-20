package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;

public record RecordDeliveryAttemptCommand(DeliveryTarget target,
                                           DeliveryType type,
                                           DeliveryStatus deliveryStatus,
                                           UserId userId,
                                           SupplierId supplierId,
                                           DepartmentId departmentId,
                                           VehicleId vehicleId,
                                           String comment,
                                           String token) {
}
