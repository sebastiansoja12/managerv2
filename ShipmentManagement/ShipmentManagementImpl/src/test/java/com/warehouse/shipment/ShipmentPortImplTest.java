package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.application.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.application.port.primary.command.ChangeShipmentTypeRequest;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentStatusRequest;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.secondary.*;
import com.warehouse.shipment.application.service.*;
import com.warehouse.shipment.application.service.delivery.ShipmentDeliveryStrategyResolver;
import com.warehouse.shipment.application.service.returning.*;
import com.warehouse.shipment.application.service.status.*;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentLimits;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static com.warehouse.shipment.DataTestCreator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentPortImplTest {

    @Mock
    private PathFinderServicePort pathFinderServicePort;

    @Mock
    private RouteLogService routeLogService;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ReturningServicePort returningServicePort;

    @Mock
    private MailNotificationServicePort mailNotificationServicePort;

    @Mock
    private SpecificationRepository specificationShipmentRepository;

    @Mock
    private OperatorContextProvider operatorContextProvider;

    @Mock
    private ShipmentDeliveryStrategyResolver shipmentDeliveryStrategyResolver;

    @Mock
    private TrackingNumberGenerationService trackingNumberGenerationService;

    @Mock
    private ShipmentConfigurationPort shipmentConfigurationPort;

    @Mock
    private CountryServiceAvailabilityService countryServiceAvailabilityService;

    @Mock
    private PriceService priceService;

    @Mock
    private SignatureService signatureService;

    @Mock
    private Logger logger;

    @Mock
    private DomainEventPublisher domainEventPublisher;

    private ShipmentPortImpl shipmentPort;

    private static final String SHIPMENT_WAS_NOT_FOUND = "Shipment not found";

    @BeforeEach
    void setUp() {
		final ShipmentStatusChangeStrategyResolver shipmentStatusChangeStrategyResolver =
				new ShipmentStatusChangeStrategyResolver(List.of(
						new ShipmentCreatedStatusChangeStrategy(),
						new ShipmentRedirectedStatusChangeStrategy(),
						new ShipmentReroutedStatusChangeStrategy(),
						new ShipmentSentStatusChangeStrategy(),
						new ShipmentDeliveredStatusChangeStrategy(),
						new ShipmentReturnedStatusChangeStrategy(),
						new ShipmentUnchangedStatusChangeStrategy()));
		final ShipmentReturnStrategyResolver shipmentReturnStrategyResolver =
				new ShipmentReturnStrategyResolver(List.of(
						new ShipmentReturnCreatedStrategy(),
						new ShipmentReturnCompletedStrategy(),
						new ShipmentReturnCancelledStrategy(),
						new ShipmentReturnUnchangedStrategy()));
		shipmentPort = new ShipmentPortImpl(shipmentRepository, specificationShipmentRepository,
				this.logger, pathFinderServicePort, this.priceService, this.countryServiceAvailabilityService,
                this.signatureService, routeLogService, returningServicePort, mailNotificationServicePort,
                this.trackingNumberGenerationService, this.shipmentConfigurationPort,
                operatorContextProvider, shipmentDeliveryStrategyResolver, shipmentStatusChangeStrategyResolver,
                shipmentReturnStrategyResolver, this.domainEventPublisher);
	}

    @Test
    void shouldShip() {
        final ShipmentCreateCommand request = shipmentCreateCommand();
        final OperatorShipmentConfiguration configuration = permissiveConfiguration();
        final TrackingNumber trackingNumber = new TrackingNumber("MGR-100");
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration()).thenReturn(configuration);
        when(this.countryServiceAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);
        when(this.countryServiceAvailabilityService.isCountryAvailable(CountryCode.DE)).thenReturn(true);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.success(new VoronoiResponse(new DepartmentCode("KT2"))));
        when(this.trackingNumberGenerationService.generate(eq(configuration.trackingNumberRule()), any(ShipmentId.class)))
                .thenReturn(trackingNumber);
        final ArgumentCaptor<Shipment> shipmentCaptor = ArgumentCaptor.forClass(Shipment.class);

        final Result<ShipmentCreateResponse, ErrorCode> response = shipmentPort.ship(request);

        assertTrue(response.isSuccess());
        assertEquals(trackingNumber.value(), response.getSuccess().trackingNumber());
        verify(this.shipmentRepository).createOrUpdate(shipmentCaptor.capture());
        assertEquals(new DepartmentCode("KT2"), shipmentCaptor.getValue().getDestination());
        assertEquals(CountryCode.PL, shipmentCaptor.getValue().getOriginCountry());
        assertEquals(CountryCode.DE, shipmentCaptor.getValue().getDestinationCountry());
        verify(this.domainEventPublisher).publish(any(ShipmentCreated.class));
    }

    @Test
    void shouldChangeShipmentStatusToRedirected() {
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = shipment();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.REDIRECT);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        shipmentPort.changeShipmentStatusTo(request);
        assertEquals(ShipmentStatus.REDIRECT, shipment.getShipmentStatus());
        assertEquals(ShipmentType.CHILD, shipment.getShipmentType());
        assertNotNull(shipment.getShipmentRelatedId());
        assertTrue(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldChangeShipmentStatusToRerouted() {
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = shipment();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.REROUTE);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        shipmentPort.changeShipmentStatusTo(request);
        assertEquals(ShipmentStatus.REROUTE, shipment.getShipmentStatus());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldChangeShipmentStatusToSent() {
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = shipment();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.SENT);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        shipmentPort.changeShipmentStatusTo(request);
        assertEquals(ShipmentStatus.SENT, shipment.getShipmentStatus());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldChangeShipmentStatusToReturned() {
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = shipment();
        shipment.notifyShipmentDelivered();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.RETURN);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        shipmentPort.changeShipmentStatusTo(request);
        assertEquals(ShipmentStatus.RETURN, shipment.getShipmentStatus());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldCancelShipmentReturnByReturnId() {
        final ReturnId returnId = new ReturnId(123L);
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = shipment();
        shipment.notifyShipmentDelivered();
        shipment.notifyShipmentReturned();
        final ShipmentReturnDetails returnDetails = new ShipmentReturnDetails(
                returnId,
                shipmentId,
                "Damaged package",
                ReturnStatus.CREATED,
                "TOKEN",
                new DepartmentCode("KT1"),
                new DepartmentCode("KT1"),
                new UserId(1L),
                new UserId(2L),
                ReasonCode.DAMAGED,
                77L,
                null,
                null);
        when(returningServicePort.getReturn(returnId)).thenReturn(returnDetails);
        when(shipmentRepository.findById(shipmentId)).thenReturn(shipment);

        shipmentPort.cancelShipmentReturn(returnId);

        assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus());
        assertTrue(shipment.getLocked());
        verify(returningServicePort).getReturn(returnId);
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldChangeShipmentStatusToDelivered() {
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = shipment();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.DELIVERY);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        shipmentPort.changeShipmentStatusTo(request);
        assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus());
        assertTrue(shipment.getLocked());
        verify(shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldTryChangeShipmentStatusToCreatedAndThrowException() {
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.CREATED);
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals("Shipment already created, status cannot be changed", exception.getMessage());
    }

    @Test
    void shouldNotChangeSenderToWhenShipmentWasNotFound() {
        final ShipmentId shipmentId = shipmentId();
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.changeSenderTo(shipmentId, sender());
        final RestException exception = assertThrows(RestException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeRecipientToWhenShipmentWasNotFound() {
        final ShipmentId shipmentId = shipmentId();
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.changeRecipientTo(shipmentId, recipient());
        final RestException exception = assertThrows(RestException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentTypeToWhenShipmentWasNotFound() {
        final ShipmentId shipmentId = shipmentId();
        final ChangeShipmentTypeRequest request = new ChangeShipmentTypeRequest(shipmentId, ShipmentType.PARENT);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.changeShipmentTypeTo(request);
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToRedirect() {
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.REDIRECT);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToRerouted() {
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.REROUTE);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToSent() {
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.SENT);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToReturned() {
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.RETURN);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToDelivered() {
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.DELIVERY);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldLoadShipment() {
        final ShipmentId shipmentId = new ShipmentId(1L);
        final Shipment expectedShipment = shipment();
        when(shipmentRepository.findById(shipmentId)).thenReturn(expectedShipment);
        final Shipment shipment = shipmentPort.loadShipment(shipmentId);
        assertEquals(shipment, expectedShipment);
    }

    @Test
    void shouldNotLoadShipment() {
        final ShipmentId shipmentId = new ShipmentId(0L);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        final Executable executable = () -> shipmentPort.loadShipment(shipmentId);
        final ShipmentNotFoundException exception =
                assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldCheckIfShipmentExists() {
        final ShipmentId shipmentId = new ShipmentId(1L);
        when(shipmentRepository.exists(shipmentId)).thenReturn(true);
        final boolean exists = shipmentPort.existsShipment(shipmentId);
        assertTrue(exists);
    }

    @Test
    void shouldCheckIfShipmentNotExists() {
        final ShipmentId shipmentId = new ShipmentId(1L);
        when(shipmentRepository.exists(shipmentId)).thenReturn(false);
        final boolean exists = shipmentPort.existsShipment(shipmentId);
        assertFalse(exists);
    }

    @Test
    void shouldRejectShipmentWhenOriginCountryIsUnavailable() {
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration())
                .thenReturn(permissiveConfiguration());

        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(shipmentCreateCommand());

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE, result.getFailure());
        verifyNoInteractions(this.pathFinderServicePort);
        verify(this.shipmentRepository, never()).createOrUpdate(any());
    }

    @Test
    void shouldRejectShipmentWhenDestinationCountryIsUnavailable() {
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration())
                .thenReturn(OperatorShipmentConfiguration.defaults());
        when(this.countryServiceAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);

        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(shipmentCreateCommand());

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE, result.getFailure());
        verifyNoInteractions(this.pathFinderServicePort);
    }

    @Test
    void shouldRejectShipmentWhenPathFinderCannotDetermineDestination() {
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration())
                .thenReturn(permissiveConfiguration());
        when(this.countryServiceAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);
        when(this.countryServiceAvailabilityService.isCountryAvailable(CountryCode.DE)).thenReturn(true);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE));

        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(shipmentCreateCommand());

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE, result.getFailure());
        verify(this.shipmentRepository, never()).createOrUpdate(any());
    }

    @Test
    void shouldLoadDangerousGood() {
        final Shipment shipment = shipment();
        shipment.changeDangerousGood(DataTestCreator.dangerousGood());
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        assertTrue(this.shipmentPort.loadDangerousGood(shipmentId()).isPresent());
    }

    @Test
    void shouldReturnEmptyDangerousGoodWhenShipmentDoesNotContainOne() {
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment());

        assertTrue(this.shipmentPort.loadDangerousGood(shipmentId()).isEmpty());
    }

    @Test
    void shouldPutDangerousGoodAndPersistShipment() {
        final Shipment shipment = shipment();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        this.shipmentPort.putDangerousGood(shipmentId(), DataTestCreator.dangerousGood());

        assertNotNull(shipment.getDangerousGood());
        verify(this.shipmentRepository).createOrUpdate(shipment);
        verify(this.domainEventPublisher).publish(
                any(com.warehouse.shipment.domain.event.ShipmentDangerousGoodUpdated.class));
    }

    @Test
    void shouldDeleteDangerousGoodAndPersistShipment() {
        final Shipment shipment = shipment();
        shipment.changeDangerousGood(DataTestCreator.dangerousGood());
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        this.shipmentPort.deleteDangerousGood(shipmentId());

        assertNull(shipment.getDangerousGood());
        verify(this.shipmentRepository).createOrUpdate(shipment);
        verify(this.domainEventPublisher).publish(
                any(com.warehouse.shipment.domain.event.ShipmentDangerousGoodRemoved.class));
    }

    @Test
    void shouldLoadShipmentReturn() {
        final ReturnId returnId = new ReturnId(123L);
        final ShipmentReturnDetails details = returnDetails(returnId);
        when(this.returningServicePort.getReturn(returnId)).thenReturn(details);

        assertSame(details, this.shipmentPort.loadShipmentReturn(returnId));
    }

    @Test
    void shouldLoadShipmentReturnsPage() {
        final DepartmentCode departmentCode = new DepartmentCode("KT1");
        final ShipmentReturnPage page = new ShipmentReturnPage(List.of(), 2, 20, 0, 0);
        when(this.returningServicePort.getReturns(departmentCode, 2, 20)).thenReturn(page);

        assertSame(page, this.shipmentPort.loadShipmentReturns(departmentCode, 2, 20));
    }

    @Test
    void shouldCancelShipmentAndPublishEvent() {
        final Shipment shipment = shipment();
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration())
                .thenReturn(OperatorShipmentConfiguration.defaults());
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        this.shipmentPort.cancel(shipmentId());

        assertEquals(ShipmentStatus.CANCELED, shipment.getShipmentStatus());
        assertTrue(shipment.getLocked());
        verify(this.shipmentRepository).createOrUpdate(shipment);
        verify(this.domainEventPublisher).publish(any(ShipmentCanceled.class));
    }

    @Test
    void shouldChangeSenderAndPublishEvent() {
        final Shipment shipment = shipment();
        final com.warehouse.shipment.domain.vo.Sender newSender =
                com.warehouse.shipment.domain.vo.Sender.builder().firstName("Anna").build();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        this.shipmentPort.changeSenderTo(shipmentId(), newSender);

        assertSame(newSender, shipment.getSender());
        verify(this.shipmentRepository).createOrUpdate(shipment);
        verify(this.domainEventPublisher).publish(
                any(ShipmentSenderChanged.class));
    }

    @Test
    void shouldChangeRecipientWithoutReroutingWhenCityIsUnchanged() {
        final Shipment shipment = shipment();
        final com.warehouse.shipment.domain.vo.Recipient newRecipient = recipient();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        this.shipmentPort.changeRecipientTo(shipmentId(), newRecipient);

        assertSame(newRecipient, shipment.getRecipient());
        verifyNoInteractions(this.pathFinderServicePort);
        verify(this.shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldRerouteShipmentWhenRecipientCityChanges() {
        final Shipment shipment = shipment();
        final DepartmentCode newDestination = new DepartmentCode("PO2");
        final com.warehouse.shipment.domain.vo.Recipient newRecipient =
                com.warehouse.shipment.domain.vo.Recipient.builder().firstName("Jan").city("Poznan").build();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.success(new VoronoiResponse(newDestination)));

        this.shipmentPort.changeRecipientTo(shipmentId(), newRecipient);

        assertSame(newRecipient, shipment.getRecipient());
        assertEquals(newDestination, shipment.getDestination());
        verify(this.shipmentRepository, times(2)).createOrUpdate(shipment);
    }

    @Test
    void shouldKeepCurrentDestinationWhenRecipientReroutingFails() {
        final Shipment shipment = shipment();
        final DepartmentCode previousDestination = shipment.getDestination();
        final com.warehouse.shipment.domain.vo.Recipient newRecipient =
                com.warehouse.shipment.domain.vo.Recipient.builder().firstName("Jan").city("Poznan").build();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE));

        this.shipmentPort.changeRecipientTo(shipmentId(), newRecipient);

        assertSame(newRecipient, shipment.getRecipient());
        assertEquals(previousDestination, shipment.getDestination());
        verify(this.logger).warn(anyString(), same(newRecipient));
    }

    @Test
    void shouldCreateSignatureFromRequest() {
        final com.warehouse.shipment.application.port.primary.command.SignatureChangeRequest request =
                new com.warehouse.shipment.application.port.primary.command.SignatureChangeRequest(
                        shipmentId(), "signed", "Jan", "DOC-1");
        final ArgumentCaptor<com.warehouse.shipment.domain.model.Signature> signatureCaptor =
                ArgumentCaptor.forClass(com.warehouse.shipment.domain.model.Signature.class);

        this.shipmentPort.changeShipmentSignatureTo(request, SignatureMethod.DIGITAL);

        verify(this.signatureService).createSignature(signatureCaptor.capture());
        assertEquals(shipmentId(), signatureCaptor.getValue().getShipmentId());
        assertEquals("Jan", signatureCaptor.getValue().getSignerName());
        assertEquals("DOC-1", signatureCaptor.getValue().getDocumentReference());
        assertEquals(SignatureMethod.DIGITAL, signatureCaptor.getValue().getSignatureMethod());
        assertArrayEquals("signed".getBytes(StandardCharsets.UTF_8),
                signatureCaptor.getValue().getSignature());
    }

    @Test
    void shouldLoadShipmentByTrackingNumber() {
        final TrackingNumber trackingNumber = new TrackingNumber("MGR-10");
        final Shipment shipment = shipment();
        when(this.shipmentRepository.findByTrackingNumber(trackingNumber)).thenReturn(shipment);

        assertSame(shipment, this.shipmentPort.loadShipment(trackingNumber));
    }

    @Test
    void shouldLoadShipmentWithRouteLog() {
        final Shipment shipment = shipment();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);
        when(this.routeLogService.findByShipmentId(shipmentId())).thenReturn(Optional.empty());

        final ShipmentRouteLog routeLog = this.shipmentPort.getShipmentByShipmentId(shipmentId());

        assertSame(shipment, routeLog.shipment());
        assertNull(routeLog.routeLog());
    }

    @Test
    void shouldDelegateShipmentSearchToSpecificationRepository() {
        final ShipmentSearchCriteria criteria = mock(ShipmentSearchCriteria.class);
        final List<Shipment> shipments = List.of(shipment());
        when(this.specificationShipmentRepository.list(criteria)).thenReturn(shipments);

        assertSame(shipments, this.shipmentPort.searchShipments(criteria));
    }

    @Test
    void shouldChangeShipmentTypeWithRelatedShipment() {
        final Shipment shipment = shipment();
        final ShipmentId relatedShipmentId = new ShipmentId(22L);
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        this.shipmentPort.changeShipmentTypeTo(shipmentId(), ShipmentType.CHILD, relatedShipmentId);

        assertEquals(ShipmentType.CHILD, shipment.getShipmentType());
        assertEquals(relatedShipmentId, shipment.getShipmentRelatedId());
        assertEquals(ShipmentStatus.REDIRECT, shipment.getShipmentStatus());
        assertTrue(shipment.getLocked());
        verify(this.shipmentRepository).createOrUpdate(shipment);
    }

    @Test
    void shouldLockShipmentAndPublishEvent() {
        final Shipment shipment = shipment();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        this.shipmentPort.lockShipment(shipmentId());

        assertTrue(shipment.getLocked());
        verify(this.shipmentRepository).createOrUpdate(shipment);
        verify(this.domainEventPublisher).publish(any(ShipmentLocked.class));
    }

    @Test
    void shouldChangeDestinationAndPublishEvent() {
        final Shipment shipment = shipment();
        final DepartmentCode destination = new DepartmentCode("LU2");
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        this.shipmentPort.changeDestination(shipmentId(), destination);

        assertEquals(destination, shipment.getDestination());
        verify(this.shipmentRepository).createOrUpdate(shipment);
        verify(this.domainEventPublisher).publish(
                any(ShipmentDestinationChanged.class));
    }

    private ShipmentCreateCommand shipmentCreateCommand() {
        return new ShipmentCreateCommand(
                null,
                DataTestCreator.money(),
                recipient(),
                sender(),
                ShipmentSize.SMALL,
                CountryCode.PL,
                CountryCode.DE,
                ShipmentPriority.MEDIUM
        );
    }

    private ShipmentReturnDetails returnDetails(final ReturnId returnId) {
        return new ShipmentReturnDetails(
                returnId,
                shipmentId(),
                "Damaged package",
                ReturnStatus.CREATED,
                "TOKEN",
                new DepartmentCode("KT1"),
                new DepartmentCode("KT1"),
                new UserId(1L),
                new UserId(2L),
                ReasonCode.DAMAGED,
                77L,
                null,
                null
        );
    }

    private OperatorShipmentConfiguration permissiveConfiguration() {
        final OperatorShipmentConfiguration defaults = OperatorShipmentConfiguration.defaults();
        return new OperatorShipmentConfiguration(
                defaults.validationRules(),
                defaults.labelSettings(),
                new ShipmentLimits(0, 0, 0, 0, 0, 0, true),
                defaults.workflowSettings(),
                defaults.trackingNumberRule(),
                defaults.notificationSettings()
        );
    }

}
