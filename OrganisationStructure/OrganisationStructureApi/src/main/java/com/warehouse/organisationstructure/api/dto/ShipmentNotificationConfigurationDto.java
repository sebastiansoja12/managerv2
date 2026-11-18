package com.warehouse.organisationstructure.api.dto;

public record ShipmentNotificationConfigurationDto(
        boolean notifyRecipientOnCreated,
        boolean notifyRecipientOnDispatched,
        boolean notifyRecipientOnDelivered,
        boolean notifySenderOnException,
        ShipmentNotificationChannelDto notificationChannel
) {

    public boolean shouldSendEmailToRecipient(final boolean shouldSendEmailOnAction) {
        return shouldSendEmailOnAction && (
                notificationChannel == ShipmentNotificationChannelDto.BOTH || notificationChannel == ShipmentNotificationChannelDto.EMAIL);
    }
}
