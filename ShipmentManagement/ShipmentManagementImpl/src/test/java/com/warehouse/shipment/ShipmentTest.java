package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
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
    void shouldAddAndRemoveDangerousGoodBeforeShipmentIsSent() {
        final Shipment shipment = shipment(null);

        shipment.changeDangerousGood(dangerousGood());
        assertNotNull(shipment.getDangerousGood());

        shipment.removeDangerousGood();
        assertNull(shipment.getDangerousGood());
    }

    @ParameterizedTest
    @EnumSource(value = ShipmentStatus.class, names = {"SENT", "DELIVERY", "RETURN"})
    void shouldRejectDangerousGoodChangeForFinalShipmentStatus(final ShipmentStatus status) {
        final Shipment shipment = shipment(null);
        shipment.changeShipmentStatus(status);

        assertThrows(
                ShipmentModificationException.class,
                () -> shipment.changeDangerousGood(dangerousGood())
        );
    }

    @Test
    void shouldRejectDangerousGoodChangeForLockedShipment() {
        final Shipment shipment = shipment(null);
        shipment.lockShipment();

        assertThrows(
                ShipmentModificationException.class,
                () -> shipment.changeDangerousGood(dangerousGood())
        );
    }

    @Test
    void shouldRejectAnyChangeAfterShipmentIsDelivered() {
        final Shipment shipment = shipment(null);

        shipment.notifyShipmentDelivered();

        assertAll(
                () -> assertThrows(ShipmentModificationException.class, () -> shipment.changeSender(sender())),
                () -> assertThrows(ShipmentModificationException.class, () -> shipment.changeRecipient(recipient())),
                () -> assertThrows(ShipmentModificationException.class,
                        () -> shipment.changeShipmentStatus(ShipmentStatus.SENT)),
                () -> assertThrows(ShipmentModificationException.class,
                        () -> shipment.redirectToSender(new ShipmentId(10L),
                                new TrackingNumber("REDIRECTED-TRACKING-NUMBER"))),
                () -> assertThrows(ShipmentModificationException.class,
                        () -> shipment.changeShipmentType(ShipmentType.PARENT))
        );
    }

    @Test
    void shouldCancelShipmentWithinConfiguredCancellationWindow() {
        final Shipment shipment = shipment(null);

        shipment.cancel(workflowSettings(30), shipment.getCreatedAt().plusMinutes(10));

        assertTrue(shipment.getLocked());
    }

    @Test
    void shouldRejectShipmentCancellationAfterConfiguredCancellationWindow() {
        final Shipment shipment = shipment(null);

        final ShipmentModificationException exception = assertThrows(
                ShipmentModificationException.class,
                () -> shipment.cancel(workflowSettings(30), shipment.getCreatedAt().plusMinutes(31))
        );

        assertEquals("Shipment cancellation window expired", exception.getMessage());
    }

    @Test
    void shouldNotBypassDangerousGoodStatusRuleThroughGeneralUpdate() {
        final Shipment shipment = shipment(null);
        shipment.changeShipmentStatus(ShipmentStatus.SENT);

        assertThrows(
                ShipmentModificationException.class,
                () -> shipment.update(
                        shipment.getSender(),
                        shipment.getRecipient(),
                        ShipmentStatus.CREATED,
                        shipment.getShipmentPriority(),
                        shipment.getShipmentSize(),
                        shipment.getPrice(),
                        dangerousGood(),
                        shipment.getDestination(),
                        shipment.getSignatureRequired()
                )
        );
        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
        assertNull(shipment.getDangerousGood());
    }

    @Test
    void shouldRedirectShipmentToSenderAsNewParentShipment() {
        final Shipment shipment = shipment(null);
        final Sender originalSender = shipment.getSender();
        final Recipient originalRecipient = shipment.getRecipient();
        final ShipmentId redirectedShipmentId = new ShipmentId(10L);
        final TrackingNumber redirectedTrackingNumber = new TrackingNumber("REDIRECTED-TRACKING-NUMBER");

        final Shipment redirectedShipment = shipment.redirectToSender(redirectedShipmentId,
                redirectedTrackingNumber);

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
                new DepartmentCode("KT1"),
                null,
                ShipmentPriority.MEDIUM,
                new TrackingNumber("TEST-TRACKING-NUMBER"),
                ShipmentStatus.CREATED
        );
    }

    private ShipmentWorkflowSettings workflowSettings(final int cancellationWindowMinutes) {
        return new ShipmentWorkflowSettings(
                ShipmentStatus.CREATED,
                null,
                false,
                true,
                false,
                cancellationWindowMinutes,
                "16:00"
        );
    }
}
