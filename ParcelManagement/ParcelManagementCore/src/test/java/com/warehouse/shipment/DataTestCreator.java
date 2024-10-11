package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Parcel;
import java.time.LocalDateTime;

public class DataTestCreator {

    static ShipmentId parcelId() {
        return new ShipmentId(1L);
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
                .shipmentSize(ShipmentSize.TEST)
                .sender(sender())
                .shipmentStatus(ShipmentStatus.CREATED)
                .shipmentId(parcelId())
                .build();
    }

    static Shipment createShipmentParcel() {
		return new Shipment(sender(), recipient(), ShipmentSize.TEST, ShipmentStatus.CREATED, null, 30.0, LocalDateTime.now(),
				LocalDateTime.now(), false);
    }
}
