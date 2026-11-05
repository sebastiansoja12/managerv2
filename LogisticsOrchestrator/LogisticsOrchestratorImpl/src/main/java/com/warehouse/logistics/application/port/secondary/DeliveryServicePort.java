package com.warehouse.logistics.application.port.secondary;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.application.port.primary.result.LogisticsResponse;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;

public interface DeliveryServicePort {

    LogisticsResponse recordAttempt(final LogisticsRequest request, final UserId userId, final DepartmentId departmentId);

    void completeShipmentDelivery(final ShipmentId shipmentId, final boolean recipientSigned,
                                  final String recipientName, final String signature);

    void assignShipmentSupplier(final ShipmentId shipmentId, final SupplierId supplierId, final VehicleId vehicleId);
}
