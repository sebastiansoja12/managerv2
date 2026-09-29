package com.warehouse.shipment;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.vo.CustomerReference;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;

public class DataTestCreator {

    static ShipmentId shipmentId() {
        return new ShipmentId(1L);
    }

    static Party recipient() {
        return Party.builder()
                .firstName("test")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .countryCode(CountryCode.DE)
                .build();
    }

    static Party sender() {
        return Party.builder()
                .firstName("updatedTest")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .countryCode(CountryCode.PL)
                .build();
    }

    static Parcel createParcel() {
        return Parcel.builder()
                .recipient(recipient())
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
                relatedShipmentId,
                money(),
                locked,
                new DepartmentId(10L),
                new DepartmentId(9L),
                null,
                ShipmentPriority.MEDIUM,
                trackingNumber(),
                ShipmentStatus.CREATED
        );
    }

    static Shipment shipmentWithParcelDetails() {
        return new Shipment(
                shipmentId(), sender(), recipient(), null,
                money(), false,
                new DepartmentId(10L), new DepartmentId(9L), null,
                ShipmentPriority.MEDIUM, trackingNumber(), ShipmentStatus.CREATED,
                null, PickupMethod.DEPARTMENT, DeliveryMethod.COURIER, null, null,
                new Dimensions(new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnit.CM),
                new Weight(new BigDecimal("5.5"), WeightUnit.KG),
                new CustomerReference("ORDER-2026-12345"), "Electronics",
                new Money(new BigDecimal("2500.00"), Currency.PLN)
        );
    }

    static DangerousGood dangerousGood() {
        return dangerousGood("Rechargeable battery");
    }

    static DangerousGood dangerousGood(final String description) {
        return new DangerousGood(
                "UN3480",
                "Lithium ion batteries",
                description,
                "9",
                null,
                null,
                "II",
                BigDecimal.ONE,
                "KILOGRAM",
                1,
                "BOX",
                false,
                false,
                false,
                false,
                "2",
                null,
                null,
                "112",
                null,
                "sds",
                null,
                "ADR",
                "ROAD",
                true,
                false,
                false,
                "flammable",
                "KEEP_DRY",
                "Handle with care",
                CountryCode.PL
        );
    }
}
