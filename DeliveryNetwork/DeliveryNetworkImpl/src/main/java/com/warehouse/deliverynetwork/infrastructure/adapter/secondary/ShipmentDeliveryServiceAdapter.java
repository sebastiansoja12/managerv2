package com.warehouse.deliverynetwork.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.application.port.secondary.ShipmentDeliveryServicePort;
import com.warehouse.deliverynetwork.domain.vo.ShipmentDepartments;
import com.warehouse.logisticsdelivery.api.DeliveryApiService;
import com.warehouse.logisticsdelivery.api.dto.ShipmentDeliveryDto;
import com.warehouse.shipment.infrastructure.ShipmentApiService;

public class ShipmentDeliveryServiceAdapter implements ShipmentDeliveryServicePort {

    private final ShipmentApiService shipmentApiService;

    private final DeliveryApiService deliveryApiService;

    public ShipmentDeliveryServiceAdapter(final ShipmentApiService shipmentApiService,
                                          final DeliveryApiService deliveryApiService) {
        this.shipmentApiService = shipmentApiService;
        this.deliveryApiService = deliveryApiService;
    }

    @Override
    public ShipmentDepartments getShipmentDepartments(final TrackingNumber trackingNumber) {
        final ShipmentId shipmentId = this.shipmentApiService.getShipmentTracking(trackingNumber).shipmentId();
        final ShipmentDeliveryDto shipmentDelivery = this.deliveryApiService.getShipmentDelivery(shipmentId);
        return new ShipmentDepartments(
                shipmentId,
                trackingNumber,
                shipmentDelivery.originDepartmentId(),
                shipmentDelivery.destinationDepartmentId());
    }
}
