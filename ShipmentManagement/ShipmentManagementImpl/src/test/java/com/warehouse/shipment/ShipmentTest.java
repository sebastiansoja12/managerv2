package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.shipment.domain.enumeration.ShipmentSource;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.PackagingType;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.warehouse.shipment.DataTestCreator.*;
import static org.junit.jupiter.api.Assertions.*;

class ShipmentTest {

    @Test
    void shouldCreateShipmentWithParcelDetailsInConstructor() {
        final Dimensions dimensions = new Dimensions(
                BigDecimal.valueOf(40), BigDecimal.valueOf(30), BigDecimal.valueOf(20), LengthUnit.CM);
        final Weight weight = new Weight(BigDecimal.valueOf(5.5), WeightUnit.KG);
        final CustomerReference customerReference = new CustomerReference("ORDER-123");
        final Money declaredValue = new Money(BigDecimal.valueOf(2500), Currency.PLN);

        final Shipment shipment = new Shipment(
                shipmentId(), sender(), recipient(), null,
                money(), false,
                ShipmentPriority.MEDIUM, trackingNumber(), ShipmentStatus.CREATED,
                dimensions, weight, customerReference, "Electronics", declaredValue,
                ShipmentServiceLevel.EXPRESS, PackagingType.BOX, ShipmentSource.WEB
        );

        assertAll(
                () -> assertEquals(dimensions, shipment.getDimensions()),
                () -> assertEquals(weight, shipment.getWeight()),
                () -> assertEquals(customerReference, shipment.getCustomerReference()),
                () -> assertEquals("Electronics", shipment.getContentDescription()),
                () -> assertEquals(declaredValue, shipment.getDeclaredValue()),
                () -> assertEquals(ShipmentServiceLevel.EXPRESS, shipment.getServiceLevel()),
                () -> assertEquals(PackagingType.BOX, shipment.getPackagingType())
        );
    }

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

        shipment.markAsDelivered();

        assertAll(
                () -> assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus()),
                () -> assertTrue(shipment.getLocked()),
                () -> assertTrue(shipment.isFullyDelivered())
        );
    }

    @Test
    void shouldRejectAnyChangeAfterShipmentIsDelivered() {
        final Shipment shipment = shipment(null);

        shipment.markAsDelivered();

        assertAll(
                () -> assertThrows(ShipmentModificationException.class, () -> shipment.changeSender(sender())),
                () -> assertThrows(ShipmentModificationException.class, () -> shipment.changeRecipient(recipient())),
                () -> assertThrows(ShipmentModificationException.class,
                        () -> shipment.changeShipmentStatus(ShipmentStatus.SENT)),
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
                () -> assertEquals(shipment.getCreatedAt().plusMinutes(10), shipment.getCancelledAt()),
                () -> assertNull(shipment.getCancellationReason()),
                () -> assertTrue(shipment.getLocked())
        );
    }

    @Test
    void shouldRecordCancellationReasonAndTime() {
        final Shipment shipment = shipment(null);
        final LocalDateTime cancelledAt = shipment.getCreatedAt().plusMinutes(5);

        shipment.cancel(workflowSettings(30), cancelledAt, CancellationReason.CUSTOMER_REQUEST);

        assertAll(
                () -> assertEquals(cancelledAt, shipment.getCancelledAt()),
                () -> assertEquals(CancellationReason.CUSTOMER_REQUEST, shipment.getCancellationReason())
        );
    }

    @Test
    void shouldNotTreatAcceptedShipmentAsCancelableDraft() {
        final Shipment shipment = shipment(null);
        shipment.changeShipmentStatus(ShipmentStatus.ACCEPTED);

        assertThrows(ShipmentModificationException.class,
                () -> shipment.cancel(workflowSettings(30), shipment.getCreatedAt().plusMinutes(10)));
    }

    @Test
    void shouldRecordWhenShipmentWasAccepted() {
        final Shipment shipment = shipment(null);

        shipment.prepareShipmentToSend();
        shipment.notifyShipmentAccepted();

        assertAll(
                () -> assertEquals(ShipmentStatus.ACCEPTED, shipment.getShipmentStatus()),
                () -> assertNotNull(shipment.getAcceptedAt()),
                () -> assertEquals(shipment.getAcceptedAt(), shipment.snapshot().acceptedAt())
        );
    }

    @Test
    void shouldRerouteCreatedShipmentWithNewPartiesAndRerouteStatus() {
        final Shipment shipment = shipment(null);
        final Party newSender = sender();
        final Party newRecipient = recipient();

        shipment.reroute(newSender, newRecipient);

        assertAll(
                () -> assertEquals(ShipmentStatus.REROUTE, shipment.getShipmentStatus()),
                () -> assertSame(newSender, shipment.getSender()),
                () -> assertSame(newRecipient, shipment.getRecipient()),
                () -> assertTrue(shipment.canBeRerouted())
        );
    }

    @Test
    void shouldRejectRerouteAfterShipmentWasCollected() {
        final Shipment shipment = shipment(null);
        shipment.notifyShipmentCollected();

        assertAll(
                () -> assertFalse(shipment.canBeRerouted()),
                () -> assertThrows(ShipmentModificationException.class,
                        () -> shipment.reroute(sender(), recipient()))
        );
    }

    @Test
    void shouldRejectRerouteOfLockedShipment() {
        final Shipment shipment = shipment(null);
        shipment.lockShipment();

        assertThrows(ShipmentModificationException.class, () -> shipment.reroute(sender(), recipient()));
    }

    @Test
    void shouldMarkCreatedShipmentAsSentWhenLoadedOnPallet() {
        final Shipment shipment = shipment(null);

        final boolean changed = shipment.notifyShipmentLoadedOnPallet();

        assertAll(
                () -> assertTrue(changed),
                () -> assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus())
        );
    }

    @Test
    void shouldMarkAcceptedShipmentAsSentWhenLoadedOnPallet() {
        final Shipment shipment = shipment(null);
        shipment.changeShipmentStatus(ShipmentStatus.ACCEPTED);

        assertTrue(shipment.notifyShipmentLoadedOnPallet());
        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
    }

    @Test
    void shouldIgnoreRepeatedPalletLoadingOfSentShipment() {
        final Shipment shipment = shipment(null);
        shipment.notifyShipmentLoadedOnPallet();

        assertFalse(shipment.notifyShipmentLoadedOnPallet());
        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
    }

    @Test
    void shouldRejectPalletLoadingOfDeliveredShipment() {
        final Shipment shipment = shipment(null);
        shipment.markAsDelivered();

        assertThrows(ShipmentModificationException.class, shipment::notifyShipmentLoadedOnPallet);
    }

    @Test
    void shouldRejectPalletLoadingOfCanceledShipment() {
        final Shipment shipment = shipment(null);
        shipment.changeShipmentStatus(ShipmentStatus.CANCELED);

        final ShipmentModificationException exception = assertThrows(
                ShipmentModificationException.class,
                shipment::notifyShipmentLoadedOnPallet
        );

        assertEquals("Shipment cannot be loaded on pallet from status CANCELED", exception.getMessage());
    }

    @Test
    void shouldMarkCreatedShipmentAsSentWhenCollectedFromClient() {
        final Shipment shipment = shipment(null);

        shipment.notifyShipmentCollected();

        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
    }

    @Test
    void shouldMarkPreparedShipmentAsSentWhenCollectedFromClient() {
        final Shipment shipment = shipment(null);
        shipment.changeShipmentStatus(ShipmentStatus.PREPARED);

        shipment.notifyShipmentCollected();

        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
    }

    @Test
    void shouldRejectCollectingShipmentThatWasAlreadySent() {
        final Shipment shipment = shipment(null);
        shipment.prepareShipmentToSend();

        final ShipmentModificationException exception = assertThrows(
                ShipmentModificationException.class,
                shipment::notifyShipmentCollected
        );

        assertEquals("Shipment cannot be collected from status SENT", exception.getMessage());
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
    void shouldRedirectShipmentToSenderAsNewParentShipment() {
        final Shipment shipment = shipment(null);
        final Party originalSender = shipment.getSender();
        final Party originalRecipient = shipment.getRecipient();
        final ShipmentId redirectedShipmentId = new ShipmentId(10L);
        final TrackingNumber redirectedTrackingNumber = new TrackingNumber("REDIRECTED-TRACKING-NUMBER");
        final ExternalId<UUID> externalId = new ExternalId<>(
                UUID.fromString("00000000-0000-4000-8000-000000000001"));
        final ShipmentWorkflowSettings shipmentWorkflowSettings = ShipmentWorkflowSettings.defaults();

        final Shipment redirectedShipment = shipment.redirectToSender(redirectedShipmentId, redirectedTrackingNumber,
                externalId, shipmentWorkflowSettings);

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
    void shouldChangeSender() {
        final Shipment shipment = shipment(null);
        final Party newSender = Party.builder().firstName("new sender").build();

        shipment.changeSender(newSender);

        assertSame(newSender, shipment.getSender());
    }

    @Test
    void shouldChangeRecipient() {
        final Shipment shipment = shipment(null);
        final Party newRecipient = Party.builder().firstName("new recipient").build();

        shipment.changeRecipient(newRecipient);

        assertSame(newRecipient, shipment.getRecipient());
    }

    @Test
    void shouldChangePrice() {
        final Shipment shipment = shipment(null);
        final Money newPrice = new Money(BigDecimal.valueOf(25), Currency.EUR);

        shipment.changePrice(newPrice);

        assertSame(newPrice, shipment.getPrice());
    }

    @Test
    void shouldUpdateSenderAndRecipientFromShipmentUpdate() {
        final Shipment shipment = shipment(null);
        final Party newSender = Party.builder().firstName("updated sender").build();
        final Party newRecipient = Party.builder().firstName("updated recipient").build();

        shipment.update(new ShipmentUpdate(newSender, newRecipient, "token"));

        assertAll(
                () -> assertSame(newSender, shipment.getSender()),
                () -> assertSame(newRecipient, shipment.getRecipient())
        );
    }

    @Test
    void shouldUpdateAllEditableShipmentData() {
        final Shipment shipment = shipment(null);
        final Party newSender = Party.builder().firstName("updated sender").build();
        final Party newRecipient = Party.builder().firstName("updated recipient").build();
        final Money newPrice = new Money(BigDecimal.valueOf(99), Currency.GBP);
        final Dimensions newDimensions = new Dimensions(
                BigDecimal.valueOf(40), BigDecimal.valueOf(30), BigDecimal.valueOf(20), LengthUnit.CM);
        final Weight newWeight = new Weight(BigDecimal.valueOf(5.5), WeightUnit.KG);
        final CustomerReference newCustomerReference = new CustomerReference("ORDER-123");
        final Money newDeclaredValue = new Money(BigDecimal.valueOf(2500), Currency.PLN);

        shipment.update(
                newSender,
                newRecipient,
                ShipmentStatus.ACCEPTED,
                ShipmentPriority.EXPRESS,
                newPrice,
                true,
                newDimensions,
                newWeight,
                newCustomerReference,
                "Electronics",
                newDeclaredValue,
                ShipmentServiceLevel.EXPRESS,
                PackagingType.BOX
        );

        assertAll(
                () -> assertSame(newSender, shipment.getSender()),
                () -> assertSame(newRecipient, shipment.getRecipient()),
                () -> assertEquals(ShipmentStatus.ACCEPTED, shipment.getShipmentStatus()),
                () -> assertEquals(ShipmentPriority.EXPRESS, shipment.getShipmentPriority()),
                () -> assertSame(newPrice, shipment.getPrice()),
                () -> assertTrue(shipment.getSignatureRequired()),
                () -> assertEquals(newDimensions, shipment.getDimensions()),
                () -> assertEquals(newWeight, shipment.getWeight()),
                () -> assertEquals(newCustomerReference, shipment.getCustomerReference()),
                () -> assertEquals("Electronics", shipment.getContentDescription()),
                () -> assertEquals(newDeclaredValue, shipment.getDeclaredValue()),
                () -> assertEquals(ShipmentServiceLevel.EXPRESS, shipment.getServiceLevel()),
                () -> assertEquals(PackagingType.BOX, shipment.getPackagingType())
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
        shipment.markAsDelivered();

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

        assertEquals("Cannot return shipment in current status", exception.getMessage());
    }

    @Test
    void shouldCancelShipmentReturn() {
        final Shipment shipment = shipment(null);
        shipment.markAsDelivered();
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
    void shouldUpdateShipmentCountries() {
        final Shipment shipment = shipment(null);

        shipment.updateCountries(new ShipmentCountryRequest(shipment.getShipmentId(), CountryCode.FR, CountryCode.CZ));

        assertAll(
                () -> assertEquals(CountryCode.FR, shipment.getSender().getAddress().getCountryCode()),
                () -> assertEquals(CountryCode.CZ, shipment.getRecipient().getAddress().getCountryCode())
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
        final ExternalId<UUID> externalId = new ExternalId<>(
                UUID.fromString("00000000-0000-4000-8000-000000000001"));
        final ShipmentWorkflowSettings workflowSettings = ShipmentWorkflowSettings.defaults();

        shipment.redirectToSender(new ShipmentId(31L), newTrackingNumber, externalId, workflowSettings);

        assertAll(
                () -> assertNotEquals(previousExternalShipmentId, shipment.getExternalShipmentId()),
                () -> assertEquals(externalId, shipment.getExternalShipmentId()),
                () -> assertEquals(newTrackingNumber, shipment.getTrackingNumber())
        );
    }

    private Shipment shipment(final ShipmentId relatedShipmentId) {
        return new Shipment(
                shipmentId(),
                sender(),
                recipient(),
                relatedShipmentId,
                money(),
                false,
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
