package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreatePalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RevertPalletDeliveryCommand;
import com.warehouse.palletmanagement.api.event.PalletLoadedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.PalletLoadingRevertedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadedEventData;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadingRevertedEventData;
import org.springframework.stereotype.Component;

@Component
public class PalletDeliveryEventListener {

    private final DeliveryPort deliveryPort;

    public PalletDeliveryEventListener(final DeliveryPort deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.pallet-loaded:pallet.loaded}",
            groupId = "${manager.kafka.consumer-groups.pallet-loaded-delivery:logistics-orchestrator-pallet}"
    )
    public void handle(final PalletLoadedIntegrationEvent event) {
        final PalletLoadedEventData payload = event.payload();
        final CreatePalletDeliveryCommand command = new CreatePalletDeliveryCommand(
                payload.palletId(),
                payload.shipmentIds().stream().map(ShipmentId::new).toList(),
                departmentId(payload.originDepartmentId()),
                departmentId(payload.destinationDepartmentId()),
                event.userId());
        deliveryPort.createPalletDelivery(command);
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.pallet-loading-reverted:pallet.loading-reverted}",
            groupId = "${manager.kafka.consumer-groups.pallet-loading-reverted-delivery:logistics-orchestrator-pallet-reverted}"
    )
    public void handle(final PalletLoadingRevertedIntegrationEvent event) {
        final PalletLoadingRevertedEventData payload = event.payload();
        deliveryPort.revertPalletDelivery(new RevertPalletDeliveryCommand(
                payload.palletId(),
                payload.shipmentIds().stream().map(ShipmentId::new).toList(),
                event.userId()));
    }

    private static DepartmentId departmentId(final Long departmentId) {
        return departmentId == null ? null : new DepartmentId(departmentId);
    }
}
