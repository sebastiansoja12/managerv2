package com.warehouse.shipment;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;

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
}
