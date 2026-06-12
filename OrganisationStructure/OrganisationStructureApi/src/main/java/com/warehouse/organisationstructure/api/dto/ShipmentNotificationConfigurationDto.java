package com.warehouse.organisationstructure.api.dto;

public record ShipmentNotificationConfigurationDto(
        boolean notifyRecipientOnCreated,
        boolean notifyRecipientOnDispatched,
        boolean notifyRecipientOnDelivered,
        boolean notifySenderOnException,
        ShipmentNotificationChannelDto notificationChannel
) {
}
