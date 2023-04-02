package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.vo.Notification;

public interface NotificationCreatorService {

    Notification createNotification(Parcel parcel, String message);
}
