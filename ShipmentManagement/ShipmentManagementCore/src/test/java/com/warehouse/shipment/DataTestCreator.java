package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Parcel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DataTestCreator {

    static ShipmentId shipmentId() {
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
                .build();
    }

    static Money money() {
        return new Money(new BigDecimal(10L), Currency.PLN);
    }

    static Shipment createShipment() {
		return new Shipment(shipmentId(), sender(), recipient(), ShipmentSize.TEST, ShipmentStatus.CREATED, ShipmentType.PARENT, null, money(), LocalDateTime.now(),
				LocalDateTime.now(), false, CountryCode.AL, CountryCode.AD, "KT1", null, false, ShipmentPriority.LOW, null, null, null);
    }
}
