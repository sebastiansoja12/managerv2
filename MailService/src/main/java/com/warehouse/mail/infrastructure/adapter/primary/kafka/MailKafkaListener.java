package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.ShipmentMailRequestMapper;
import com.warehouse.reroute.api.event.ShipmentRerouteRequestedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDeliveredIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentOutForDeliveryIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;

public class MailKafkaListener {

    private final MailPort mailPort;
    private final ShipmentMailRequestMapper shipmentMailRequestMapper;

    public MailKafkaListener(final MailPort mailPort, final ShipmentMailRequestMapper shipmentMailRequestMapper) {
        this.mailPort = mailPort;
        this.shipmentMailRequestMapper = shipmentMailRequestMapper;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-created:shipment.created}",
            groupId = "${manager.mail.kafka.consumer-groups.shipment-created:manager-mail-shipment-created}"
    )
    public void handle(final ShipmentCreatedIntegrationEvent event) {
        this.mailPort.send(this.shipmentMailRequestMapper.mapCreated(event.payload()));
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-events:shipment.events}",
            groupId = "${manager.mail.kafka.consumer-groups.shipment-status-changed:manager-mail-shipment-status-changed}"
    )
    public void handle(final ShipmentChangedIntegrationEvent event) {
        if (event instanceof ShipmentStatusChangedIntegrationEvent statusChanged) {
            this.mailPort.send(this.shipmentMailRequestMapper.mapStatusChanged(statusChanged.payload()));
        }
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-out-for-delivery:shipment.out-for-delivery}",
            groupId = "${manager.mail.kafka.consumer-groups.shipment-out-for-delivery:manager-mail-shipment-out-for-delivery}"
    )
    public void handle(final ShipmentOutForDeliveryIntegrationEvent event) {
        this.mailPort.send(this.shipmentMailRequestMapper.mapOutForDelivery(event.payload()));
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-delivered:shipment.delivered}",
            groupId = "${manager.mail.kafka.consumer-groups.shipment-delivered:manager-mail-shipment-delivered}"
    )
    public void handle(final ShipmentDeliveredIntegrationEvent event) {
        this.mailPort.send(this.shipmentMailRequestMapper.mapDelivered(event.payload()));
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-reroute-requested:shipment.reroute.requested}",
            groupId = "${manager.mail.kafka.consumer-groups.shipment-reroute-requested:manager-mail-shipment-reroute-requested}"
    )
    public void handle(final ShipmentRerouteRequestedIntegrationEvent event) {
        this.mailPort.send(this.shipmentMailRequestMapper.mapRerouteRequested(event));
    }
}
