package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.money;
import static com.warehouse.shipment.DataTestCreator.recipient;
import static com.warehouse.shipment.DataTestCreator.sender;
import static com.warehouse.shipment.DataTestCreator.shipmentId;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;

class ShipmentTest {

    @Test
    void shouldCreateParentShipmentWhenRelatedShipmentIsMissing() {
        final Shipment shipment = shipment(null);

        assertAll(
                () -> assertEquals(ShipmentStatus.CREATED, shipment.getShipmentStatus()),
                () -> assertEquals(ShipmentType.PARENT, shipment.getShipmentType()),
                () -> assertNull(shipment.getShipmentRelatedId()),
                () -> assertFalse(shipment.getLocked()),
                () -> assertFalse(shipment.getSignatureRequired()),
                () -> assertNotNull(shipment.getExternalShipmentId())
        );
    }

    @Test
    void shouldCreateChildShipmentWhenRelatedShipmentExists() {
        final ShipmentId relatedShipmentId = new ShipmentId(2L);

        final Shipment shipment = shipment(relatedShipmentId);

        assertAll(
                () -> assertEquals(ShipmentStatus.CREATED, shipment.getShipmentStatus()),
                () -> assertEquals(ShipmentType.CHILD, shipment.getShipmentType()),
                () -> assertEquals(relatedShipmentId, shipment.getShipmentRelatedId())
        );
    }

    @Test
    void shouldChangeShipmentTypeToParentAndClearRelation() {
        final Shipment shipment = shipment(new ShipmentId(2L));
        final LocalDateTime previousUpdatedAt = shipment.getUpdatedAt();

        shipment.changeShipmentType(ShipmentType.PARENT);

        assertAll(
                () -> assertEquals(ShipmentType.PARENT, shipment.getShipmentType()),
                () -> assertNull(shipment.getShipmentRelatedId()),
                () -> assertFalse(shipment.getLocked()),
                () -> assertTrue(shipment.getUpdatedAt().isAfter(previousUpdatedAt)
                        || shipment.getUpdatedAt().isEqual(previousUpdatedAt))
        );
    }

    @Test
    void shouldChangeShipmentTypeToChildWithRelatedShipmentAndLockShipment() {
        final Shipment shipment = shipment(null);
        final ShipmentId relatedShipmentId = new ShipmentId(2L);

        shipment.changeShipmentTypeWithRelatedId(ShipmentType.CHILD, relatedShipmentId);

        assertAll(
                () -> assertEquals(ShipmentType.CHILD, shipment.getShipmentType()),
                () -> assertEquals(relatedShipmentId, shipment.getShipmentRelatedId()),
                () -> assertTrue(shipment.getLocked())
        );
    }

    @Test
    void shouldMarkDeliveredShipmentAsFullyDelivered() {
        final Shipment shipment = shipment(null);

        shipment.notifyShipmentDelivered();

        assertAll(
                () -> assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus()),
                () -> assertTrue(shipment.getLocked()),
                () -> assertTrue(shipment.isFullyDelivered())
        );
    }

    @Test
    void shouldRedirectShipmentToSenderAsNewParentShipment() {
        final Shipment shipment = shipment(null);
        final Sender originalSender = shipment.getSender();
        final Recipient originalRecipient = shipment.getRecipient();
        final ShipmentId redirectedShipmentId = new ShipmentId(10L);

        final Shipment redirectedShipment = shipment.redirectToSender(redirectedShipmentId);

        assertAll(
                () -> assertEquals(shipment, redirectedShipment),
                () -> assertEquals(redirectedShipmentId, redirectedShipment.getShipmentId()),
                () -> assertEquals(ShipmentType.PARENT, redirectedShipment.getShipmentType()),
                () -> assertEquals(ShipmentStatus.CREATED, redirectedShipment.getShipmentStatus()),
                () -> assertEquals(originalRecipient.getFirstName(), redirectedShipment.getSender().getFirstName()),
                () -> assertEquals(originalSender.getFirstName(), redirectedShipment.getRecipient().getFirstName())
        );
    }

    private Shipment shipment(final ShipmentId relatedShipmentId) {
        return new Shipment(
                shipmentId(),
                sender(),
                recipient(),
                ShipmentSize.SMALL,
                relatedShipmentId,
                CountryCode.PL,
                CountryCode.DE,
                money(),
                false,
                "KT1",
                null,
                ShipmentPriority.MEDIUM,
                new TrackingNumber("TEST-TRACKING-NUMBER")
        );
    }
}
