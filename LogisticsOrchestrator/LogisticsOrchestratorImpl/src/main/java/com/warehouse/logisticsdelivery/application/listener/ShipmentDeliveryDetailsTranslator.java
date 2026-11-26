package com.warehouse.logisticsdelivery.application.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import com.warehouse.logisticsdelivery.application.port.secondary.CourierServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
import com.warehouse.logisticsdelivery.domain.event.DeliveryReadModelRebuilt;
import com.warehouse.logisticsdelivery.domain.vo.AssignedCourier;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.shipment.api.event.ShipmentDeliveryReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryReadModelData;

@Component
public class ShipmentDeliveryDetailsTranslator {

    private final CourierServicePort courierServicePort;

    public ShipmentDeliveryDetailsTranslator(final CourierServicePort courierServicePort) {
        this.courierServicePort = courierServicePort;
    }

    @IntegrationEventTranslation
    public List<ShipmentDeliveryReadModelChanged> translate(final DeliveryChanged event) {
        return shipmentDeliveryDetails(event.getSnapshot());
    }

    @IntegrationEventTranslation
    public List<ShipmentDeliveryReadModelChanged> translate(final DeliveryReadModelRebuilt event) {
        return shipmentDeliveryDetails(event.getSnapshot());
    }

    private List<ShipmentDeliveryReadModelChanged> shipmentDeliveryDetails(final DeliverySnapshot delivery) {
        if (!delivery.describesCurrentShipmentDelivery()) {
            return List.of();
        }
        final AssignedCourier courier = delivery.supplierId() == null ? null
                : this.courierServicePort.findCourier(delivery.supplierId()).orElse(null);
        return List.of(new ShipmentDeliveryReadModelChanged(
                new ShipmentDeliveryReadModelData(delivery.shipmentId(), delivery.originDepartmentId(),
                        delivery.destinationDepartmentId(), delivery.currentDepartmentId(), delivery.plannedAt(),
                        delivery.deliveredAt(), courier == null ? null : courier.courierCode(),
                        courier == null ? null : courier.name())));
    }
}
