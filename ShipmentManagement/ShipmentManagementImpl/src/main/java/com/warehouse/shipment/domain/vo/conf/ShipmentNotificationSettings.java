package com.warehouse.shipment.domain.vo.conf;

import java.util.Objects;

public record ShipmentNotificationSettings(
        boolean notifyRecipientOnCreated,
        boolean notifyRecipientOnDispatched,
        boolean notifyRecipientOnDelivered,
        boolean notifySenderOnException,
        ShipmentNotificationChannel notificationChannel
) {

    public ShipmentNotificationSettings {
        notificationChannel = Objects.requireNonNullElse(notificationChannel, ShipmentNotificationChannel.SMS);
    }

    public static ShipmentNotificationSettings defaults() {
        return new ShipmentNotificationSettings(true, true, true, true, ShipmentNotificationChannel.SMS);
    }
}
