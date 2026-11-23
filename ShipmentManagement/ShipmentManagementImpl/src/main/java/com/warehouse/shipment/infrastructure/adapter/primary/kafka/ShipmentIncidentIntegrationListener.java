package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
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

    @IntegrationEventListener(group = "manager-shipment-incident")
    public void handle(final IncidentReported event) {
        shipmentIncidentPort.reportIncident(mapper.map(event, IncidentNoteType.REPORTED));
    }

    @IntegrationEventListener(group = "manager-shipment-incident")
    public void handle(final IncidentCommented event) {
        shipmentIncidentPort.commentIncident(mapper.map(event, IncidentNoteType.COMMENTED));
    }

    @IntegrationEventListener(group = "manager-shipment-incident")
    public void handle(final IncidentResolved event) {
        shipmentIncidentPort.resolveIncident(mapper.map(event));
    }

    @IntegrationEventListener(group = "manager-shipment-incident")
    public void handle(final IncidentReopened event) {
        shipmentIncidentPort.reopenIncident(mapper.map(event, IncidentNoteType.REOPENED));
    }
}
