package com.warehouse.routetracker;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ParcelType;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;

public class ParcelDatabaseFactory {

    public static ParcelEntity parcel(Long parcelId) {
        return ParcelEntity.builder()
                .id(parcelId)
                .parcelSize(Size.TEST)
                .firstName("test")
                .lastName("test")
                .senderTelephone("123")
                .senderCity("test")
                .senderEmail("test@wp.pl")
                .senderStreet("test")
                .senderPostalCode("00-000")
                .recipientFirstName("test")
                .recipientLastName("test")
                .recipientCity("test")
                .recipientEmail("test@wp.pl")
                .recipientStreet("test")
                .recipientPostalCode("00-000")
                .recipientTelephone("1233")
                .destination("KT1")
                .status(Status.REDIRECT)
                .parcelType(ParcelType.CHILD)
                .parcelRelatedId(null)
                .status(Status.CREATED)
                .build();
    }
}
