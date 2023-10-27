package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.domain.model.Parcel;

public interface NotificationCreatorProvider {

    Notification createNotification(Parcel parcel, String message);

    Notification createRerouteNotification(Parcel parcel, String message);
}
