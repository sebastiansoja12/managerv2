package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletId;

public record ChangePalletDeliveryRouteCommand(PalletId palletId, DepartmentId originDepartmentId,
                                               DepartmentId destinationDepartmentId) {
}
