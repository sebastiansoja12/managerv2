package com.warehouse.logistics.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.logistics.domain.model.CreatePalletDeliveryCommand;
import com.warehouse.logistics.domain.port.primary.LogisticsPort;
import com.warehouse.palletmanagement.api.event.PalletLoadedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadedEventData;
import org.springframework.stereotype.Component;

@Component
public class PalletDeliveryEventListener {

    private final LogisticsPort logisticsPort;
    private final OperatorContext operatorContext;

    public PalletDeliveryEventListener(final LogisticsPort logisticsPort,
                                       final OperatorContext operatorContext) {
        this.logisticsPort = logisticsPort;
        this.operatorContext = operatorContext;
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
                event.userId());
        this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(),
                () -> logisticsPort.createPalletDelivery(command));
    }
}
