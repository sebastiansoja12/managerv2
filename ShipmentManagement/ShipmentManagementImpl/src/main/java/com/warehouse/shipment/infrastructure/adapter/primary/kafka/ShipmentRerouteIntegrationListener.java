package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.reroute.api.event.ShipmentRerouteRequestedIntegrationEvent;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.vo.ShipmentOwnerContext;
import com.warehouse.shipment.infrastructure.adapter.primary.kafka.mapper.ShipmentRerouteEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ShipmentRerouteIntegrationListener {

    private final ShipmentPort shipmentPort;

    private final OperatorContext operatorContext;

    private final ShipmentRerouteEventMapper mapper;

    public ShipmentRerouteIntegrationListener(final ShipmentPort shipmentPort,
                                              final OperatorContext operatorContext) {
        this.shipmentPort = shipmentPort;
        this.operatorContext = operatorContext;
        this.mapper = new ShipmentRerouteEventMapper();
    }

    @KafkaEventListener(
            topics = "shipment.reroute.requested",
            groupId = "manager-shipment-reroute"
    )
    public void handle(final ShipmentRerouteRequestedIntegrationEvent event) {
        final TrackingNumber trackingNumber = mapper.mapTrackingNumber(event);
        final ShipmentOwnerContext context = this.shipmentPort.findContext(trackingNumber);
        if (context == null) {
            log.warn("Reroute skipped, shipment {} was not found", event.trackingNumber());
            return;
        }

        operatorContext.runAs(context.operatorId(), null, null, () -> shipmentPort.reroute(mapper.map(event)));
        log.info("Shipment {} rerouted by {}", event.trackingNumber(), event.sender().email());
    }
}
