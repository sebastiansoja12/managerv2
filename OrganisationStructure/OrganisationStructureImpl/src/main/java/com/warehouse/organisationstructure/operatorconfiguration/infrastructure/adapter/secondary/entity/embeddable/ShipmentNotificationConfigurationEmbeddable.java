package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.NotificationChannel;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentNotificationConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class ShipmentNotificationConfigurationEmbeddable {

    @Column(name = "shipment_notify_recipient_on_created")
    private boolean notifyRecipientOnCreated;

    @Column(name = "shipment_notify_recipient_on_dispatched")
    private boolean notifyRecipientOnDispatched;

    @Column(name = "shipment_notify_recipient_on_delivered")
    private boolean notifyRecipientOnDelivered;

    @Column(name = "shipment_notify_sender_on_exception")
    private boolean notifySenderOnException;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_notification_channel")
    private NotificationChannel notificationChannel;

    public ShipmentNotificationConfigurationEmbeddable() {
    }

    public static ShipmentNotificationConfigurationEmbeddable from(
            final ShipmentNotificationConfiguration configuration) {
        final ShipmentNotificationConfiguration source = configuration != null
                ? configuration
                : ShipmentNotificationConfiguration.defaultConfiguration();
        final ShipmentNotificationConfigurationEmbeddable embeddable =
                new ShipmentNotificationConfigurationEmbeddable();
        embeddable.notifyRecipientOnCreated = source.isNotifyRecipientOnCreated();
        embeddable.notifyRecipientOnDispatched = source.isNotifyRecipientOnDispatched();
        embeddable.notifyRecipientOnDelivered = source.isNotifyRecipientOnDelivered();
        embeddable.notifySenderOnException = source.isNotifySenderOnException();
        embeddable.notificationChannel = source.getNotificationChannel();
        return embeddable;
    }

    public ShipmentNotificationConfiguration toModel() {
        return new ShipmentNotificationConfiguration(
                notifyRecipientOnCreated,
                notifyRecipientOnDispatched,
                notifyRecipientOnDelivered,
                notifySenderOnException,
                notificationChannel
        );
    }
}
