package com.warehouse.reroute.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ReroutableShipment(ShipmentId shipmentId,
                                 TrackingNumber trackingNumber,
                                 ShipmentStatus status,
                                 boolean reroutable,
                                 RerouteParty sender,
                                 RerouteParty recipient) {

    public String notificationEmail() {
        return sender != null && sender.email() != null && !sender.email().isBlank() ? sender.email().trim() : null;
    }

    public String maskedNotificationEmail() {
        final String email = notificationEmail();
        if (email == null) {
            return null;
        }
        final int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(atIndex);
    }
}
