package com.warehouse.mail.domain.vo;

public record ShipmentNotificationPolicy(boolean notifyRecipientOnCreated,
                                         boolean notifyRecipientOnOutForDelivery,
                                         boolean notifyRecipientOnDelivered) {

    public static ShipmentNotificationPolicy unrestricted() {
        return new ShipmentNotificationPolicy(true, true, true);
    }
}
