package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.ShipmentSize;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.ShipmentStatus;

import java.time.LocalDateTime;

public class DataTestCreator {

    static ParcelId parcelId() {
        return new ParcelId(1L);
    }

    static Recipient recipient() {
        return Recipient.builder()
                .firstName("test")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

    static Sender sender() {
        return Sender.builder()
                .firstName("updatedTest")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

    static Parcel createParcel() {
        return Parcel.builder()
                .recipient(recipient())
                .parcelShipmentSize(ShipmentSize.TEST)
                .sender(sender())
                .parcelShipmentStatus(ShipmentStatus.CREATED)
                .id(parcelId())
                .build();
    }

    static ShipmentParcel createShipmentParcel() {
		return new ShipmentParcel(sender(), recipient(), ShipmentSize.TEST, ShipmentStatus.CREATED, null, 30.0, LocalDateTime.now(),
				LocalDateTime.now(), false);
    }
}
