package com.warehouse.pallet.infrastructure.adapter.primary.kafka;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.PalletRouteChangedRequest;
import com.warehouse.pallet.domain.port.primary.PalletPort;
import com.warehouse.pallet.infrastructure.adapter.primary.kafka.event.DeliveryChangedIntegrationEvent;
import com.warehouse.pallet.infrastructure.adapter.primary.kafka.event.DeliveryEventData;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryRouteChangedKafkaListener {

    private static final String DELIVERY_MODIFIED = "delivery.modified";
    private static final String PALLET_TARGET = "PALLET";

    private final PalletPort palletPort;

    public DeliveryRouteChangedKafkaListener(final PalletPort palletPort) {
        this.palletPort = palletPort;
    }

    @KafkaListener(
            topics = "${manager.kafka.topics.delivery-events:delivery.events}",
            groupId = "${spring.kafka.consumer.group-id:pallet-handling-manager}"
    )
    public void handle(final DeliveryChangedIntegrationEvent event) {
        final DeliveryEventData payload = event.payload();
        if (!DELIVERY_MODIFIED.equals(event.eventType()) || payload == null
                || !PALLET_TARGET.equals(payload.targetType())) {
            return;
        }
        this.palletPort.handleDeliveryRouteChanged(new PalletRouteChangedRequest(event.eventId(),
                new PalletId(payload.targetId()), payload.originDepartmentId(), payload.destinationDepartmentId()));
    }
}
