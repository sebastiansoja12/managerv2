package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentNotificationConfiguration {
    private boolean notifyRecipientOnCreated;
    private boolean notifyRecipientOnDispatched;
    private boolean notifyRecipientOnDelivered;
    private boolean notifySenderOnException;
    private NotificationChannel notificationChannel;

    public ShipmentNotificationConfiguration() {
    }

    public ShipmentNotificationConfiguration(final boolean notifyRecipientOnCreated,
                                             final boolean notifyRecipientOnDispatched,
                                             final boolean notifyRecipientOnDelivered,
                                             final boolean notifySenderOnException,
                                             final NotificationChannel notificationChannel) {
        this.notifyRecipientOnCreated = notifyRecipientOnCreated;
        this.notifyRecipientOnDispatched = notifyRecipientOnDispatched;
        this.notifyRecipientOnDelivered = notifyRecipientOnDelivered;
        this.notifySenderOnException = notifySenderOnException;
        this.notificationChannel = notificationChannel;
    }

    public static ShipmentNotificationConfiguration defaultConfiguration() {
        return new ShipmentNotificationConfiguration(true, true, true, true, NotificationChannel.SMS);
    }

    public boolean isNotifyRecipientOnCreated() { return notifyRecipientOnCreated; }
    public boolean isNotifyRecipientOnDispatched() { return notifyRecipientOnDispatched; }
    public boolean isNotifyRecipientOnDelivered() { return notifyRecipientOnDelivered; }
    public boolean isNotifySenderOnException() { return notifySenderOnException; }
    public NotificationChannel getNotificationChannel() { return notificationChannel; }
}
