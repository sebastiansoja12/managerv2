package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentCountryRequest;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;

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

        assertAll(
                () -> assertEquals(ShipmentStatus.CANCELED, shipment.getShipmentStatus()),
                () -> assertTrue(shipment.getLocked())
        );
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
                () -> assertSame(shipment, redirectedShipment),
                () -> assertEquals(redirectedShipmentId, redirectedShipment.getShipmentId()),
                () -> assertEquals(ShipmentType.PARENT, redirectedShipment.getShipmentType()),
                () -> assertEquals(ShipmentStatus.CREATED, redirectedShipment.getShipmentStatus()),
                () -> assertEquals(originalRecipient.getFirstName(), redirectedShipment.getSender().getFirstName()),
                () -> assertEquals(originalSender.getFirstName(), redirectedShipment.getRecipient().getFirstName())
        );
    }

    @Test
    void shouldPrepareShipmentToCreate() {
        final Shipment shipment = shipment(null);
        shipment.changeShipmentStatus(ShipmentStatus.PREPARED);

        shipment.prepareShipmentToCreate();

        assertEquals(ShipmentStatus.CREATED, shipment.getShipmentStatus());
    }

    @Test
    void shouldPrepareShipmentToReroute() {
        final Shipment shipment = shipment(null);

        shipment.prepareShipmentToReroute();

        assertEquals(ShipmentStatus.REROUTE, shipment.getShipmentStatus());
    }

    @Test
    void shouldPrepareShipmentToRedirectAndLockIt() {
        final Shipment shipment = shipment(null);
        final ShipmentId relatedShipmentId = new ShipmentId(20L);

        shipment.prepareShipmentToRedirect(relatedShipmentId);

        assertAll(
                () -> assertEquals(ShipmentStatus.REDIRECT, shipment.getShipmentStatus()),
                () -> assertEquals(ShipmentType.CHILD, shipment.getShipmentType()),
                () -> assertEquals(relatedShipmentId, shipment.getShipmentRelatedId()),
                () -> assertTrue(shipment.getLocked())
        );
    }

    @Test
    void shouldPrepareShipmentToSend() {
        final Shipment shipment = shipment(null);

        shipment.prepareShipmentToSend();

        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
    }

    @Test
    void shouldPrepareShipmentToDeliverWithoutLockingIt() {
        final Shipment shipment = shipment(null);

        shipment.prepareShipmentToDeliver();

        assertAll(
                () -> assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus()),
                () -> assertFalse(shipment.getLocked()),
                () -> assertFalse(shipment.isFullyDelivered())
        );
    }

    @Test
    void shouldLockShipment() {
        final Shipment shipment = shipment(null);

        shipment.lockShipment();

        assertTrue(shipment.getLocked());
    }

    @Test
    void shouldChangeSignature() {
        final Shipment shipment = shipment(null);
        final Signature signature = new Signature();

        shipment.changeSignature(signature);

        assertSame(signature, shipment.getSignature());
    }

    @Test
    void shouldChangeSender() {
        final Shipment shipment = shipment(null);
        final Sender newSender = Sender.builder().firstName("new sender").build();

        shipment.changeSender(newSender);

        assertSame(newSender, shipment.getSender());
    }

    @Test
    void shouldChangeRecipient() {
        final Shipment shipment = shipment(null);
        final Recipient newRecipient = Recipient.builder().firstName("new recipient").build();

        shipment.changeRecipient(newRecipient);

        assertSame(newRecipient, shipment.getRecipient());
    }

    @Test
    void shouldChangeShipmentSize() {
        final Shipment shipment = shipment(null);

        shipment.changeShipmentSize(ShipmentSize.BIG);

        assertEquals(ShipmentSize.BIG, shipment.getShipmentSize());
    }

    @Test
    void shouldChangePrice() {
        final Shipment shipment = shipment(null);
        final Money newPrice = new Money(BigDecimal.valueOf(25), Currency.EUR);

        shipment.changePrice(newPrice);

        assertSame(newPrice, shipment.getPrice());
    }

    @Test
    void shouldUpdateDestinationFromVoronoiResponse() {
        final Shipment shipment = shipment(null);
        final DepartmentCode newDestination = new DepartmentCode("KR1");

        shipment.updateDestination(new VoronoiResponse(newDestination));

        assertEquals(newDestination, shipment.getDestination());
    }

    @Test
    void shouldIgnoreMissingVoronoiResponse() {
        final Shipment shipment = shipment(null);
        final DepartmentCode previousDestination = shipment.getDestination();
        final LocalDateTime previousUpdatedAt = shipment.getUpdatedAt();

        shipment.updateDestination(null);

        assertAll(
                () -> assertEquals(previousDestination, shipment.getDestination()),
                () -> assertEquals(previousUpdatedAt, shipment.getUpdatedAt())
        );
    }

    @Test
    void shouldUpdateSenderAndRecipientFromShipmentUpdate() {
        final Shipment shipment = shipment(null);
        final Sender newSender = Sender.builder().firstName("updated sender").build();
        final Recipient newRecipient = Recipient.builder().firstName("updated recipient").build();

        shipment.update(new ShipmentUpdate(newSender, newRecipient, "KR1", "token"));

        assertAll(
                () -> assertSame(newSender, shipment.getSender()),
                () -> assertSame(newRecipient, shipment.getRecipient())
        );
    }

    @Test
    void shouldUpdateAllEditableShipmentData() {
        final Shipment shipment = shipment(null);
        final Sender newSender = Sender.builder().firstName("updated sender").build();
        final Recipient newRecipient = Recipient.builder().firstName("updated recipient").build();
        final Money newPrice = new Money(BigDecimal.valueOf(99), Currency.GBP);
        final DepartmentCode newDestination = new DepartmentCode("GD1");

        shipment.update(
                newSender,
                newRecipient,
                ShipmentStatus.ACCEPTED,
                ShipmentPriority.EXPRESS,
                ShipmentSize.CUSTOM,
                newPrice,
                dangerousGood(),
                newDestination,
                true
        );

        assertAll(
                () -> assertSame(newSender, shipment.getSender()),
                () -> assertSame(newRecipient, shipment.getRecipient()),
                () -> assertEquals(ShipmentStatus.ACCEPTED, shipment.getShipmentStatus()),
                () -> assertEquals(ShipmentPriority.EXPRESS, shipment.getShipmentPriority()),
                () -> assertEquals(ShipmentSize.CUSTOM, shipment.getShipmentSize()),
                () -> assertSame(newPrice, shipment.getPrice()),
                () -> assertNotNull(shipment.getDangerousGood()),
                () -> assertEquals(newDestination, shipment.getDestination()),
                () -> assertTrue(shipment.getSignatureRequired())
        );
    }

    @Test
    void shouldChangeShipmentStatus() {
        final Shipment shipment = shipment(null);

        shipment.changeShipmentStatus(ShipmentStatus.ACCEPTED);

        assertEquals(ShipmentStatus.ACCEPTED, shipment.getShipmentStatus());
    }

    @Test
    void shouldApplyRelatedShipmentRedirectNotification() {
        final Shipment shipment = shipment(null);
        final ShipmentId relatedShipmentId = new ShipmentId(21L);

        shipment.notifyRelatedShipmentRedirected(relatedShipmentId);

        assertAll(
                () -> assertEquals(relatedShipmentId, shipment.getShipmentRelatedId()),
                () -> assertEquals(ShipmentType.CHILD, shipment.getShipmentType()),
                () -> assertEquals(ShipmentStatus.REDIRECT, shipment.getShipmentStatus()),
                () -> assertTrue(shipment.getLocked())
        );
    }

    @Test
    void shouldApplyRelatedShipmentLockedNotification() {
        final Shipment shipment = shipment(null);
        shipment.changeShipmentTypeWithRelatedId(ShipmentType.CHILD, new ShipmentId(22L));

        shipment.notifyRelatedShipmentLocked();

        assertAll(
                () -> assertEquals(ShipmentType.PARENT, shipment.getShipmentType()),
                () -> assertNull(shipment.getShipmentRelatedId()),
                () -> assertFalse(shipment.getLocked())
        );
    }

    @Test
    void shouldApplyShipmentReroutedNotification() {
        final Shipment shipment = shipment(null);

        shipment.notifyShipmentRerouted();

        assertEquals(ShipmentStatus.REROUTE, shipment.getShipmentStatus());
    }

    @Test
    void shouldApplyShipmentSentNotification() {
        final Shipment shipment = shipment(null);

        shipment.notifyShipmentSent();

        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
    }

    @Test
    void shouldReturnDeliveredShipment() {
        final Shipment shipment = shipment(null);
        shipment.notifyShipmentDelivered();

        shipment.notifyShipmentReturned();

        assertAll(
                () -> assertEquals(ShipmentStatus.RETURN, shipment.getShipmentStatus()),
                () -> assertTrue(shipment.getLocked())
        );
    }

    @Test
    void shouldRejectReturnForShipmentThatWasNotDelivered() {
        final Shipment shipment = shipment(null);

        final ShipmentModificationException exception = assertThrows(
                ShipmentModificationException.class,
                shipment::notifyShipmentReturned
        );

        assertEquals("Cannot return for not delivered shipment", exception.getMessage());
    }

    @Test
    void shouldCancelShipmentReturn() {
        final Shipment shipment = shipment(null);
        shipment.notifyShipmentDelivered();
        shipment.notifyShipmentReturned();

        shipment.notifyShipmentReturnCanceled();

        assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus());
    }

    @Test
    void shouldRejectReturnCancellationForShipmentThatWasNotReturned() {
        final Shipment shipment = shipment(null);

        final ShipmentModificationException exception = assertThrows(
                ShipmentModificationException.class,
                shipment::notifyShipmentReturnCanceled
        );

        assertEquals("Cannot undone return for not returned shipment", exception.getMessage());
    }

    @Test
    void shouldChangeCurrency() {
        final Shipment shipment = shipment(null);

        shipment.changeCurrency(Currency.EUR);

        assertEquals(Currency.EUR, shipment.getPrice().getCurrency());
    }

    @Test
    void shouldChangeSignatureRequirement() {
        final Shipment shipment = shipment(null);

        shipment.changeSignatureRequired(true);

        assertTrue(shipment.getSignatureRequired());
    }

    @Test
    void shouldChangeShipmentPriority() {
        final Shipment shipment = shipment(null);

        shipment.changeShipmentPriority(ShipmentPriority.HIGH);

        assertEquals(ShipmentPriority.HIGH, shipment.getShipmentPriority());
    }

    @Test
    void shouldChangeDestinationDepartment() {
        final Shipment shipment = shipment(null);
        final DepartmentCode destination = new DepartmentCode("WA1");

        shipment.changeDestinationDepartment(destination);

        assertEquals(destination, shipment.getDestination());
    }

    @Test
    void shouldUpdateShipmentCountries() {
        final Shipment shipment = shipment(null);

        shipment.updateCountries(new ShipmentCountryRequest(shipment.getShipmentId(), CountryCode.FR, CountryCode.CZ));

        assertAll(
                () -> assertEquals(CountryCode.FR, shipment.getOriginCountry()),
                () -> assertEquals(CountryCode.CZ, shipment.getDestinationCountry())
        );
    }

    @Test
    void shouldExposeCurrentStateAsSnapshot() {
        final Shipment shipment = shipment(new ShipmentId(30L));

        final ShipmentSnapshot snapshot = shipment.snapshot();

        assertAll(
                () -> assertEquals(shipment.getShipmentId(), snapshot.shipmentId()),
                () -> assertSame(shipment.getSender(), snapshot.sender()),
                () -> assertSame(shipment.getRecipient(), snapshot.recipient()),
                () -> assertEquals(shipment.getShipmentStatus(), snapshot.shipmentStatus()),
                () -> assertEquals(shipment.getShipmentType(), snapshot.shipmentType()),
                () -> assertEquals(shipment.getShipmentRelatedId(), snapshot.shipmentRelatedId()),
                () -> assertEquals(shipment.getTrackingNumber(), snapshot.trackingNumber()),
                () -> assertEquals(shipment.getExternalShipmentId(), snapshot.externalShipmentId())
        );
    }

    @Test
    void shouldGenerateNewExternalIdAndUseNewTrackingNumberWhenRedirectingToSender() {
        final Shipment shipment = shipment(null);
        final ExternalId<UUID> previousExternalShipmentId = shipment.getExternalShipmentId();
        final TrackingNumber newTrackingNumber = new TrackingNumber("RETURN-TRACKING-NUMBER");

        shipment.redirectToSender(new ShipmentId(31L), newTrackingNumber);

        assertAll(
                () -> assertNotEquals(previousExternalShipmentId, shipment.getExternalShipmentId()),
                () -> assertEquals(newTrackingNumber, shipment.getTrackingNumber())
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
