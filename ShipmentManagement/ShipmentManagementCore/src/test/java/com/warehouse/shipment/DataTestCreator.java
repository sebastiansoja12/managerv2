package com.warehouse.shipment;

import java.math.BigDecimal;
import java.util.List;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.Money;
import com.warehouse.commonassets.model.Weight;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.DangerousGoodId;
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

    static TrackingNumber trackingNumber() {
        return new TrackingNumber("TEST-TRACKING-NUMBER");
    }

    static Shipment shipment() {
        return shipment(null, false);
    }

    static Shipment shipment(final ShipmentId relatedShipmentId) {
        return shipment(relatedShipmentId, false);
    }

    static Shipment lockedShipment() {
        return shipment(null, true);
    }

    static Shipment shipment(final ShipmentId relatedShipmentId, final Boolean locked) {
        return new Shipment(
                shipmentId(),
                sender(),
                recipient(),
                ShipmentSize.SMALL,
                relatedShipmentId,
                CountryCode.PL,
                CountryCode.DE,
                money(),
                locked,
                "KT1",
                null,
                ShipmentPriority.MEDIUM,
                trackingNumber()
        );
    }

    static DangerousGood dangerousGood() {
        return new DangerousGood(
                new DangerousGoodId(1L),
                shipmentId(),
                "Lithium battery",
                "Rechargeable battery",
                "9",
                List.of("flammable"),
                "KEEP_DRY",
                "Handle with care",
                new Weight(BigDecimal.ONE, WeightUnit.KILOGRAM),
                "BOX",
                true,
                false,
                false,
                "112",
                CountryCode.PL,
                "sds"
        );
    }
}
