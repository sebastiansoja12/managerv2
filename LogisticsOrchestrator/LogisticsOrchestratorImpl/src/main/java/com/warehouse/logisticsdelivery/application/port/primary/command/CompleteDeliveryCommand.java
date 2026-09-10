package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;

public record CompleteDeliveryCommand(ShipmentId shipmentId,
                                      SupplierId supplierId,
                                      UserId userId,
                                      DepartmentId departmentId) {
}
