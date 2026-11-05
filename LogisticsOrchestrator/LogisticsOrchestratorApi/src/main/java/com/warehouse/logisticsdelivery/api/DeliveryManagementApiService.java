package com.warehouse.logisticsdelivery.api;

import java.util.List;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.api.dto.DeliveryConditionsChangeDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryDetailsDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryRouteChangeDto;

public interface DeliveryManagementApiService {

    List<DeliveryDetailsDto> findDeliveries(final DepartmentId departmentId, final SupplierId supplierId,
                                            final int offset, final int limit);

    DeliveryDetailsDto findDelivery(final DeliveryId deliveryId);

    DeliveryDetailsDto findDeliveryByShipment(final ShipmentId shipmentId);

    DeliveryDetailsDto assignSupplier(final DeliveryId deliveryId, final SupplierId supplierId,
                                      final VehicleId vehicleId);

    DeliveryDetailsDto unassignSupplier(final DeliveryId deliveryId);

    DeliveryDetailsDto changeRoute(final DeliveryId deliveryId, final DeliveryRouteChangeDto route);

    DeliveryDetailsDto changeConditions(final DeliveryId deliveryId, final DeliveryConditionsChangeDto conditions);

    DeliveryDetailsDto commentStep(final DeliveryId deliveryId, final int stepNumber, final String comment);

    DeliveryDetailsDto correctStepStatus(final DeliveryId deliveryId, final int stepNumber,
                                         final String deliveryStatus);

    void cancelDelivery(final DeliveryId deliveryId);
}
