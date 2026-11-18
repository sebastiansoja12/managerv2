package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.application.port.secondary.ShipmentNotificationPolicyPort;
import com.warehouse.mail.domain.vo.ShipmentNotificationPolicy;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.ShipmentMailRequestMapper;
import com.warehouse.reroute.api.event.ShipmentRerouteRequestedIntegrationEvent;
import com.warehouse.shipment.api.event.*;

public class MailKafkaListener {

    private final MailPort mailPort;
    private final ShipmentMailRequestMapper shipmentMailRequestMapper;
    private final ShipmentNotificationPolicyPort shipmentNotificationPolicyPort;

    public MailKafkaListener(final MailPort mailPort, final ShipmentMailRequestMapper shipmentMailRequestMapper,
                             final ShipmentNotificationPolicyPort shipmentNotificationPolicyPort) {
        this.mailPort = mailPort;
        this.shipmentMailRequestMapper = shipmentMailRequestMapper;
        this.shipmentNotificationPolicyPort = shipmentNotificationPolicyPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-created:shipment.created}",
            groupId = "${manager.mail.kafka.consumer-groups.shipment-created:manager-mail-shipment-created}"
    )
    public void handle(final ShipmentCreatedIntegrationEvent event) {
        final ShipmentNotificationPolicy policy = policyFor(event);
        this.mailPort.send(this.shipmentMailRequestMapper.mapCreated(event.payload(),
                policy.notifyRecipientOnCreated()));
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
        final ShipmentNotificationPolicy policy = policyFor(event);
        this.mailPort.send(this.shipmentMailRequestMapper.mapOutForDelivery(event.payload(),
                policy.notifyRecipientOnOutForDelivery()));
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-delivered:shipment.delivered}",
            groupId = "${manager.mail.kafka.consumer-groups.shipment-delivered:manager-mail-shipment-delivered}"
    )
    public void handle(final ShipmentDeliveredIntegrationEvent event) {
        final ShipmentNotificationPolicy policy = policyFor(event);
        this.mailPort.send(this.shipmentMailRequestMapper.mapDelivered(event.payload(),
                policy.notifyRecipientOnDelivered()));
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-reroute-requested:shipment.reroute.requested}",
            groupId = "${manager.mail.kafka.consumer-groups.shipment-reroute-requested:manager-mail-shipment-reroute-requested}"
    )
    public void handle(final ShipmentRerouteRequestedIntegrationEvent event) {
        this.mailPort.send(this.shipmentMailRequestMapper.mapRerouteRequested(event));
    }

    private ShipmentNotificationPolicy policyFor(final ShipmentChangedIntegrationEvent event) {
        return this.shipmentNotificationPolicyPort.policyFor(event.operatorId());
    }
}
