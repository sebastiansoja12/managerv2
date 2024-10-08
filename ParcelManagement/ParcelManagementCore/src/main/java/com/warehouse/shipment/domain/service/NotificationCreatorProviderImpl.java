package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.vo.ConstantBodyMailMessage;
import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.domain.vo.Parcel;

import lombok.AllArgsConstructor;

@AllArgsConstructor
// TODO
public class NotificationCreatorProviderImpl implements NotificationCreatorProvider {

    private final static String SUBJECT = "Została nadana do państwa przesyłka: ";

    @Override
    public Notification createNotification(Parcel parcel) {
        final ConstantBodyMailMessage constantBodyMailMessage = new ConstantBodyMailMessage(parcel);

        return Notification.builder()
                .body(constantBodyMailMessage.getMessage())
                .recipient(parcel.getRecipient().email())
                .subject(SUBJECT)
                .build();
    }

    @Override
    public Notification createRerouteNotification(Parcel parcel) {
        final ConstantBodyMailMessage constantBodyMailMessage = new ConstantBodyMailMessage(parcel);

        return Notification.builder()
                .body(constantBodyMailMessage.getMessage())
                .recipient(parcel.getRecipient().email())
                .subject(SUBJECT)
                .build();
    }
}
