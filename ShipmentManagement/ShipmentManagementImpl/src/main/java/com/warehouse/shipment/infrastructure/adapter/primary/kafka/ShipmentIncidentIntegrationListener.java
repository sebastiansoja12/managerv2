package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.shipment.api.event.IncidentChanged;
import com.warehouse.shipment.api.event.IncidentCommented;
import com.warehouse.shipment.api.event.IncidentReopened;
import com.warehouse.shipment.api.event.IncidentReported;
import com.warehouse.shipment.api.event.IncidentResolved;
import com.warehouse.shipment.application.port.primary.ShipmentIncidentPort;
import com.warehouse.shipment.domain.enumeration.IncidentNoteType;
import com.warehouse.shipment.infrastructure.adapter.primary.kafka.mapper.ShipmentIncidentEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShipmentIncidentIntegrationListener {

    private final ShipmentIncidentPort shipmentIncidentPort;

    private final ShipmentIncidentEventMapper mapper;

    public ShipmentIncidentIntegrationListener(final ShipmentIncidentPort shipmentIncidentPort) {
        this.shipmentIncidentPort = shipmentIncidentPort;
        this.mapper = new ShipmentIncidentEventMapper();
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-incident-events:shipment.incident.events}",
            groupId = "${manager.kafka.consumer-groups.shipment-incident-events:manager-shipment-incident}"
    )
    public void handle(final IncidentChanged event) {
        switch (event) {
            case IncidentReported reported ->
                    shipmentIncidentPort.reportIncident(mapper.map(reported, IncidentNoteType.REPORTED));
            case IncidentCommented commented ->
                    shipmentIncidentPort.commentIncident(mapper.map(commented, IncidentNoteType.COMMENTED));
            case IncidentResolved resolved -> shipmentIncidentPort.resolveIncident(mapper.map(resolved));
            case IncidentReopened reopened ->
                    shipmentIncidentPort.reopenIncident(mapper.map(reopened, IncidentNoteType.REOPENED));
            default -> log.warn("Unsupported incident event {}", event.getClass().getSimpleName());
        }
    }
}
