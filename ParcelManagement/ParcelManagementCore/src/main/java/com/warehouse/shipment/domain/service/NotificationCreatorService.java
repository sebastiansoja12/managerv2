package com.warehouse.shipment.domain.service;

import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.shipment.domain.model.Parcel;

public interface NotificationCreatorService {

    Notification createNotification(Parcel parcel, String message);

    Notification createRerouteNotification(Parcel parcel, String message);
}
