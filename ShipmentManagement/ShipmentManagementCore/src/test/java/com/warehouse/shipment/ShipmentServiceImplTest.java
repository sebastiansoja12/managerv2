package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.ShipmentNotFoundException;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.registry.DomainContext;
import com.warehouse.shipment.domain.service.ShipmentServiceImpl;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentCountryRequest;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private SpecificationRepository specificationShipmentRepository;

    private ShipmentServiceImpl shipmentService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(DomainContext.class, "eventPublisher", eventPublisher);
        shipmentService = new ShipmentServiceImpl(shipmentRepository, specificationShipmentRepository);
    }

    @Test
    void shouldCreateShipmentAndPublishEvent() {
        final Shipment shipment = shipment();

        shipmentService.createShipment(shipment);

        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentCreatedEvent.class);
    }

    @Test
    void shouldFindShipmentById() {
        final Shipment expectedShipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(expectedShipment);

        final Shipment foundShipment = shipmentService.find(shipmentId());

        assertEquals(expectedShipment, foundShipment);
    }

    @Test
    void shouldFindShipmentByTrackingNumber() {
        final Shipment expectedShipment = shipment();
        final TrackingNumber trackingNumber = trackingNumber();
        when(shipmentRepository.findByTrackingNumber(trackingNumber)).thenReturn(expectedShipment);

        final Shipment foundShipment = shipmentService.find(trackingNumber);

        assertEquals(expectedShipment, foundShipment);
    }

    @Test
    void shouldCheckIfShipmentExists() {
        when(shipmentRepository.exists(shipmentId())).thenReturn(true);

        final boolean exists = shipmentService.existsShipment(shipmentId());

        assertTrue(exists);
    }

    @Test
    void shouldChangeSenderAndPublishEvent() {
        final Shipment shipment = shipment();
        final Sender sender = sender();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeSenderTo(shipmentId(), sender);

        assertEquals(sender, shipment.getSender());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentSenderChanged.class);
    }

    @Test
    void shouldChangeRecipientAndPublishEvent() {
        final Shipment shipment = shipment();
        final Recipient recipient = recipient();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeRecipientTo(shipmentId(), recipient);

        assertEquals(recipient, shipment.getRecipient());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentRecipientChanged.class);
    }

    @Test
    void shouldChangeShipmentTypeToParentAndPublishEvent() {
        final Shipment shipment = shipment(new ShipmentId(2L), true);
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeShipmentTypeTo(shipmentId(), ShipmentType.PARENT, null);

        assertEquals(ShipmentType.PARENT, shipment.getShipmentType());
        assertNull(shipment.getShipmentRelatedId());
        assertFalse(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentTypeChanged.class);
    }

    @Test
    void shouldChangeShipmentTypeToChildWithRelatedShipmentAndPublishEvent() {
        final Shipment shipment = shipment();
        final ShipmentId relatedShipmentId = new ShipmentId(2L);
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeShipmentTypeTo(shipmentId(), ShipmentType.CHILD, relatedShipmentId);

        assertEquals(ShipmentType.CHILD, shipment.getShipmentType());
        assertEquals(relatedShipmentId, shipment.getShipmentRelatedId());
        assertTrue(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentTypeChanged.class);
    }

    @Test
    void shouldChangeShipmentStatusAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeShipmentStatusTo(shipmentId(), ShipmentStatus.SENT);

        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentStatusChangedEvent.class);
    }

    @Test
    void shouldChangeShipmentRelatedId() {
        final Shipment shipment = shipment();
        final ShipmentId relatedShipmentId = new ShipmentId(2L);
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeShipmentRelatedIdTo(shipmentId(), relatedShipmentId);

        assertEquals(relatedShipmentId, shipment.getShipmentRelatedId());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldChangeShipmentPriority() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeShipmentPriorityTo(shipmentId(), ShipmentPriority.EXPRESS);

        assertEquals(ShipmentPriority.EXPRESS, shipment.getShipmentPriority());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldChangeCurrencyAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeCurrencyTo(shipmentId(), Currency.EUR);

        assertEquals(Currency.EUR, shipment.getPrice().getCurrency());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentCurrencyChanged.class);
    }

    @Test
    void shouldChangeShipmentIssuerCountryAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeShipmentIssuerCountryTo(shipmentId(), CountryCode.FR);

        assertEquals(CountryCode.FR, shipment.getOriginCountry());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentCountriesChanged.class);
    }

    @Test
    void shouldChangeShipmentReceiverCountryAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeShipmentReceiverCountryTo(shipmentId(), CountryCode.FR);

        assertEquals(CountryCode.FR, shipment.getDestinationCountry());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentCountriesChanged.class);
    }

    @Test
    void shouldChangeSignatureRequired() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeSignatureRequiredTo(shipmentId(), true);

        assertTrue(shipment.getSignatureRequired());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldChangeDangerousGood() {
        final Shipment shipment = shipment();
        final DangerousGood dangerousGood = dangerousGood();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeDangerousGoodTo(shipmentId(), dangerousGood);

        assertEquals(dangerousGood, shipment.getDangerousGood());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldNotifyRelatedShipmentRedirectedAndPublishEvent() {
        final Shipment shipment = shipment();
        final ShipmentId relatedShipmentId = new ShipmentId(2L);
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.notifyRelatedShipmentRedirected(shipmentId(), relatedShipmentId);

        assertEquals(ShipmentStatus.REDIRECT, shipment.getShipmentStatus());
        assertEquals(ShipmentType.CHILD, shipment.getShipmentType());
        assertEquals(relatedShipmentId, shipment.getShipmentRelatedId());
        assertTrue(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentRedirected.class);
    }

    @Test
    void shouldNotifyShipmentReroutedAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.notifyShipmentRerouted(shipmentId());

        assertEquals(ShipmentStatus.REROUTE, shipment.getShipmentStatus());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentRerouted.class);
    }

    @Test
    void shouldNotifyRelatedShipmentLockedAndPublishEvent() {
        final Shipment shipment = shipment(new ShipmentId(2L), true);
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.notifyRelatedShipmentLocked(shipmentId());

        assertEquals(ShipmentType.PARENT, shipment.getShipmentType());
        assertNull(shipment.getShipmentRelatedId());
        assertFalse(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentRelatedLocked.class);
    }

    @Test
    void shouldNotifyShipmentSentAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.notifyShipmentSent(shipmentId());

        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentSent.class);
    }

    @Test
    void shouldNotifyShipmentReturnedAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.notifyShipmentReturned(shipmentId());

        assertEquals(ShipmentStatus.RETURN, shipment.getShipmentStatus());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentReturned.class);
    }

    @Test
    void shouldNotifyShipmentReturnCreatedAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.notifyShipmentReturned(shipmentId(), "Damaged package", ReasonCode.DAMAGED);

        assertEquals(ShipmentStatus.RETURN, shipment.getShipmentStatus());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentReturnCreated.class);
    }

    @Test
    void shouldNotifyShipmentDeliveredAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.notifyShipmentDelivered(shipmentId());

        assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus());
        assertTrue(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentDelivered.class);
    }

    @Test
    void shouldNotifyReturnCanceledAndPublishEvent() {
        final Shipment shipment = shipment(null, true);
        shipment.changeShipmentStatus(ShipmentStatus.RETURN);
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.notifyReturnCanceled(shipmentId());

        assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus());
        assertFalse(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentReturnCanceled.class);
    }

    @Test
    void shouldChangeShipmentCountriesAndPublishEvent() {
        final Shipment shipment = shipment();
        final ShipmentCountryRequest request = new ShipmentCountryRequest(shipmentId(), CountryCode.FR, CountryCode.ES);
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeShipmentCountries(request);

        assertEquals(CountryCode.FR, shipment.getOriginCountry());
        assertEquals(CountryCode.ES, shipment.getDestinationCountry());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentCountriesChanged.class);
    }

    @Test
    void shouldLockShipmentAndPublishEvent() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.lockShipment(shipmentId());

        assertTrue(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentLocked.class);
    }

    @Test
    void shouldGenerateNextShipmentId() {
        final ShipmentId nextShipmentId = shipmentService.nextShipmentId();

        assertNotNull(nextShipmentId);
        assertNotNull(nextShipmentId.getValue());
    }

    @Test
    void shouldUpdateShipmentAndPublishEvent() {
        final Shipment shipment = shipment();

        shipmentService.update(shipment);

        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentUpdated.class);
    }

    @Test
    void shouldChangeRouteProcessId() {
        final Shipment shipment = shipment();
        final ProcessId processId = new ProcessId(UUID.randomUUID());
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeRouteProcessId(processId, shipmentId());

        assertEquals(processId.getValue().toString(), shipment.getExternalRouteId().value());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldAssignExternalReturnId() {
        final Shipment shipment = shipment();
        final ReturnId returnId = new ReturnId(12L);
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.assignExternalReturnId(shipmentId(), returnId);

        assertEquals(returnId.getId(), shipment.getExternalReturnId().value());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldRedirectShipmentToSenderAndPublishEvent() {
        final ShipmentId relatedShipmentId = new ShipmentId(2L);
        final Shipment shipment = shipment(relatedShipmentId);
        final String originalSenderFirstName = shipment.getSender().getFirstName();
        final String originalRecipientFirstName = shipment.getRecipient().getFirstName();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.redirectShipmentToSender(shipmentId());

        assertEquals(relatedShipmentId, shipment.getShipmentId());
        assertEquals(ShipmentType.PARENT, shipment.getShipmentType());
        assertEquals(ShipmentStatus.CREATED, shipment.getShipmentStatus());
        assertEquals(originalRecipientFirstName, shipment.getSender().getFirstName());
        assertEquals(originalSenderFirstName, shipment.getRecipient().getFirstName());
        verify(shipmentRepository).createOrUpdate(shipment);
        assertEventPublished(ShipmentRedirected.class);
    }

    @Test
    void shouldChangeDestination() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentService.changeDestination(shipmentId(), "KR1");

        assertEquals("KR1", shipment.getDestination());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldFindShipmentByExternalId() {
        final ExternalId<String> externalId = ExternalId.generateId();
        final Shipment expectedShipment = shipment();
        when(shipmentRepository.findByExternalId(externalId)).thenReturn(Optional.of(expectedShipment));

        final Shipment foundShipment = shipmentService.findByExternalId(externalId);

        assertEquals(expectedShipment, foundShipment);
    }

    @Test
    void shouldThrowExceptionWhenShipmentByExternalIdDoesNotExist() {
        final ExternalId<String> externalId = ExternalId.generateId();
        when(shipmentRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class,
                () -> shipmentService.findByExternalId(externalId));

        assertEquals("Shipment not found", exception.getMessage());
    }

    private void assertEventPublished(final Class<?> eventClass) {
        verify(eventPublisher).publishEvent(argThat((Object event) -> eventClass.isInstance(event)));
    }
}
