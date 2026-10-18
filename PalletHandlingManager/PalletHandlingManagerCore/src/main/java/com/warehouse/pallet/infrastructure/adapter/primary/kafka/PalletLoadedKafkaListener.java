package com.warehouse.pallet.infrastructure.adapter.primary.kafka;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.PalletLoadedRequest;
import com.warehouse.pallet.domain.port.primary.PalletPort;
import com.warehouse.pallet.infrastructure.adapter.primary.kafka.event.PalletLoadedIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PalletLoadedKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(PalletLoadedKafkaListener.class);

    private final PalletPort palletPort;

    public PalletLoadedKafkaListener(final PalletPort palletPort) {
        this.palletPort = palletPort;
    }

    @KafkaListener(
            topics = "${manager.kafka.topics.pallet-loaded:pallet.loaded}",
            groupId = "${spring.kafka.consumer.group-id:pallet-handling-manager}"
    )
    public void handle(final PalletLoadedIntegrationEvent event) {
        if (event == null || event.payload() == null || event.payload().palletId() == null) {
            log.warn("Ignoring pallet loaded event without pallet id: {}", event == null ? null : event.eventId());
            return;
        }
        this.palletPort.handlePalletLoaded(
                new PalletLoadedRequest(event.eventId(), new PalletId(event.payload().palletId())));
    }
}
