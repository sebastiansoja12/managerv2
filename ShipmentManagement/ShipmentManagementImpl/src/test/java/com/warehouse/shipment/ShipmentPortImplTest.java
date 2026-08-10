package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.returning.api.dto.LongValueDto;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.returning.api.dto.StringValueDto;
import com.warehouse.shipment.application.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.application.port.primary.command.ChangeShipmentTypeRequest;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentStatusRequest;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.secondary.*;
import com.warehouse.shipment.application.service.*;
import com.warehouse.shipment.application.service.delivery.ShipmentDeliveryStrategyResolver;
import com.warehouse.shipment.application.service.status.*;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentLimits;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private OperatorContextProvider operatorContextProvider;

    @Mock
    private ShipmentDeliveryStrategyResolver shipmentDeliveryStrategyResolver;

    @Mock
    private TrackingNumberGenerationService trackingNumberGenerationService;

    @Mock
    private ShipmentConfigurationPort shipmentConfigurationPort;

    @Mock
    private DepartmentCountryAvailabilityService departmentCountryAvailabilityService;

    @Mock
    private SignatureService signatureService;

    @Mock
    private Logger logger;

    @Mock
    private DomainEventPublisher domainEventPublisher;

    @Mock
    private DepartmentServicePort departmentServicePort;

    private ShipmentPortImpl shipmentPort;

    private static final String SHIPMENT_WAS_NOT_FOUND = "Shipment not found";

    @BeforeEach
    void setUp() {
		final ShipmentStatusChangeStrategyResolver shipmentStatusChangeStrategyResolver =
				new ShipmentStatusChangeStrategyResolver(List.of(
						new ShipmentPlannedStatusChangeStrategy(),
						new ShipmentCreatedStatusChangeStrategy(),
						new ShipmentAcceptedStatusChangeStrategy(),
						new ShipmentRedirectedStatusChangeStrategy(),
						new ShipmentReroutedStatusChangeStrategy(),
						new ShipmentSentStatusChangeStrategy(),
						new ShipmentDeliveredStatusChangeStrategy(),
						new ShipmentReturnedStatusChangeStrategy(),
						new ShipmentPreparedStatusChangeStrategy(),
						new ShipmentCanceledStatusChangeStrategy()));
		final ShipmentResultFactory shipmentResultFactory = new ShipmentResultFactory(
				departmentServicePort, routeLogService, returningServicePort);
		shipmentPort = new ShipmentPortImpl(shipmentRepository,
				this.logger, pathFinderServicePort, this.departmentCountryAvailabilityService,
				this.signatureService, shipmentResultFactory, mailNotificationServicePort,
                this.trackingNumberGenerationService, this.shipmentConfigurationPort,
                operatorContextProvider, shipmentDeliveryStrategyResolver, shipmentStatusChangeStrategyResolver,
                this.domainEventPublisher, departmentServicePort);
	}

    @Test
    void shouldShip() {
        final ShipmentCreateCommand request = shipmentCreateCommand();
        final OperatorShipmentConfiguration configuration = permissiveConfiguration();
        final TrackingNumber trackingNumber = new TrackingNumber("MGR-100");
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration()).thenReturn(configuration);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.DE)).thenReturn(true);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.success(new VoronoiResponse(new DepartmentCode("KT2"))));
        when(this.departmentServicePort.getDepartmentId(new DepartmentCode("KT2")))
                .thenReturn(new DepartmentId(12L));
        when(this.operatorContextProvider.currentDepartmentId()).thenReturn(Optional.of(new DepartmentId(9L)));
        when(this.trackingNumberGenerationService.generate(eq(configuration.trackingNumberRule()), any(ShipmentId.class)))
                .thenReturn(trackingNumber);
        final ArgumentCaptor<Shipment> shipmentCaptor = ArgumentCaptor.forClass(Shipment.class);

        final Result<ShipmentCreateResponse, ErrorCode> response = shipmentPort.ship(request);

        assertTrue(response.isSuccess());
        assertEquals(trackingNumber.value(), response.getSuccess().trackingNumber());
        verify(this.shipmentRepository).createOrUpdate(shipmentCaptor.capture());
        assertEquals(new DepartmentId(12L), shipmentCaptor.getValue().getTargetDepartmentId());
        assertEquals(CountryCode.PL, shipmentCaptor.getValue().getSender().getAddress().getCountryCode());
        assertEquals(CountryCode.DE, shipmentCaptor.getValue().getRecipient().getAddress().getCountryCode());
        verify(this.domainEventPublisher).publish(any(ShipmentCreated.class));
    }

    @ParameterizedTest
    @EnumSource(value = PickupMethod.class, names = {"LOCKER", "PICKUP_POINT"})
    void shouldCreatePlannedPickupPointShipmentWithDeliveryDepartment(
            final PickupMethod pickupMethod) {
        final ShipmentCreateCommand request = shipmentCreateCommand();
        request.setPickupMethod(pickupMethod);
        final OperatorShipmentConfiguration configuration = permissiveConfiguration();
        final DepartmentCode destination = new DepartmentCode("KT2");
        final DepartmentId targetDepartmentId = new DepartmentId(10L);
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration()).thenReturn(configuration);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.DE)).thenReturn(true);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.success(new VoronoiResponse(destination)));
        when(this.departmentServicePort.getDepartmentId(destination)).thenReturn(targetDepartmentId);
        when(this.trackingNumberGenerationService.generate(eq(configuration.trackingNumberRule()), any(ShipmentId.class)))
                .thenReturn(new TrackingNumber("MGR-PLANNED"));
        when(this.operatorContextProvider.currentDepartmentId()).thenReturn(Optional.empty());
        final ArgumentCaptor<Shipment> shipmentCaptor = ArgumentCaptor.forClass(Shipment.class);

        final Result<ShipmentCreateResponse, ErrorCode> result = shipmentPort.ship(request);

        assertTrue(result.isSuccess());
        verify(this.shipmentRepository).createOrUpdate(shipmentCaptor.capture());
        assertEquals(ShipmentStatus.PLANNED, shipmentCaptor.getValue().getShipmentStatus());
        assertEquals(pickupMethod, shipmentCaptor.getValue().getPickupMethod());
        assertEquals(DeliveryMethod.COURIER, shipmentCaptor.getValue().getDeliveryMethod());
        assertEquals(targetDepartmentId, shipmentCaptor.getValue().getTargetDepartmentId());
        assertNull(shipmentCaptor.getValue().getOriginDepartmentId());
        assertNull(shipmentCaptor.getValue().getPickupPointId());
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
    void shouldRejectAcceptingPlannedShipmentBeforeItIsSent() {
        final Shipment shipment = plannedShipment();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        assertThrows(com.warehouse.shipment.domain.exception.ShipmentModificationException.class,
                () -> shipmentPort.changeShipmentStatusTo(new ShipmentStatusRequest(shipmentId(), ShipmentStatus.ACCEPTED)));

        assertEquals(ShipmentStatus.PLANNED, shipment.getShipmentStatus());
        verify(shipmentRepository, never()).createOrUpdate(any());
        verifyNoInteractions(domainEventPublisher);
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
    void shouldRejectManualReturnStatusWithoutRtmProcessing() {
        final Shipment shipment = shipment();
        shipment.markAsDelivered();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        assertThrows(IllegalStateException.class,
                () -> shipmentPort.changeShipmentStatusTo(new ShipmentStatusRequest(shipmentId(), ShipmentStatus.RETURN)));

        assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus());
        verify(shipmentRepository, never()).createOrUpdate(any());
        verifyNoInteractions(domainEventPublisher);
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
        final DepartmentCode destination = new DepartmentCode("KT1");
        when(shipmentRepository.findById(shipmentId)).thenReturn(expectedShipment);
        when(this.departmentServicePort.getDepartmentCode(expectedShipment.getTargetDepartmentId()))
                .thenReturn(destination);

        final ShipmentResult shipment = shipmentPort.loadShipment(shipmentId);

        assertEquals(expectedShipment.snapshot(), shipment.snapshot());
        assertEquals(destination, shipment.destination());
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
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);

        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(shipmentCreateCommand());

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE, result.getFailure());
        verifyNoInteractions(this.pathFinderServicePort);
    }

    @ParameterizedTest
    @EnumSource(PickupMethod.class)
    void shouldRejectShipmentWhenPathFinderCannotDetermineDestination(final PickupMethod pickupMethod) {
        final ShipmentCreateCommand request = shipmentCreateCommand();
        request.setPickupMethod(pickupMethod);
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration())
                .thenReturn(permissiveConfiguration());
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.DE)).thenReturn(true);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE));

        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(request);

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE, result.getFailure());
        verify(this.shipmentRepository, never()).createOrUpdate(any());
    }

    @Test
    void shouldRejectStandardShipmentWithoutSendingDepartment() {
        final OperatorShipmentConfiguration configuration = permissiveConfiguration();
        final DepartmentCode destination = new DepartmentCode("KT2");
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration()).thenReturn(configuration);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.DE)).thenReturn(true);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.success(new VoronoiResponse(destination)));
        when(this.departmentServicePort.getDepartmentId(destination)).thenReturn(new DepartmentId(10L));
        when(this.operatorContextProvider.currentDepartmentId()).thenReturn(Optional.empty());

        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(shipmentCreateCommand());

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE, result.getFailure());
        verify(this.shipmentRepository, never()).createOrUpdate(any());
    }

    @ParameterizedTest
    @EnumSource(PickupMethod.class)
    void shouldRejectShipmentWithoutDeliveryDepartment(final PickupMethod pickupMethod) {
        final ShipmentCreateCommand request = shipmentCreateCommand();
        request.setPickupMethod(pickupMethod);
        final OperatorShipmentConfiguration configuration = permissiveConfiguration();
        final DepartmentCode destination = new DepartmentCode("KT2");
        when(this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration()).thenReturn(configuration);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.PL)).thenReturn(true);
        when(this.departmentCountryAvailabilityService.isCountryAvailable(CountryCode.DE)).thenReturn(true);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.success(new VoronoiResponse(destination)));

        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(request);

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
        final com.warehouse.shipment.domain.vo.Party newSender =
                com.warehouse.shipment.domain.vo.Party.builder().firstName("Anna").build();
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
        final com.warehouse.shipment.domain.vo.Party newRecipient = recipient();
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
        final com.warehouse.shipment.domain.vo.Party newRecipient =
                com.warehouse.shipment.domain.vo.Party.builder().firstName("Jan").city("Poznan").build();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.success(new VoronoiResponse(newDestination)));
        when(this.departmentServicePort.getDepartmentId(newDestination)).thenReturn(new DepartmentId(12L));

        this.shipmentPort.changeRecipientTo(shipmentId(), newRecipient);

        assertSame(newRecipient, shipment.getRecipient());
        assertEquals(new DepartmentId(12L), shipment.getTargetDepartmentId());
        verify(this.shipmentRepository, times(2)).createOrUpdate(shipment);
    }

    @Test
    void shouldKeepCurrentDestinationWhenRecipientReroutingFails() {
        final Shipment shipment = shipment();
        final DepartmentId previousDestination = shipment.getTargetDepartmentId();
        final com.warehouse.shipment.domain.vo.Party newRecipient =
                com.warehouse.shipment.domain.vo.Party.builder().firstName("Jan").city("Poznan").build();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);
        when(this.pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE));

        this.shipmentPort.changeRecipientTo(shipmentId(), newRecipient);

        assertSame(newRecipient, shipment.getRecipient());
        assertEquals(previousDestination, shipment.getTargetDepartmentId());
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

        final ShipmentResult result = this.shipmentPort.loadShipment(trackingNumber);

        assertEquals(shipment.snapshot(), result.snapshot());
    }

    @Test
    void shouldLoadShipmentWithRouteLogAndCheckForExistingReturn() {
        final Shipment shipment = shipment();
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);
        when(this.routeLogService.findByShipmentId(shipmentId())).thenReturn(Optional.empty());

        final ShipmentRouteLog routeLog = this.shipmentPort.loadShipmentWithRouteLog(shipmentId());

        assertEquals(shipment.snapshot(), routeLog.shipment().snapshot());
        assertNull(routeLog.routeLog());
        assertNull(routeLog.returnPackage());
        verify(this.returningServicePort).findReturnByShipmentId(shipmentId());
    }

    @Test
    void shouldIncludeReturnInShipmentDetailsById() {
        final Shipment shipment = returnedShipment();
        final ReturnDetailsDto details = returnDetails(new ReturnId(123L));
        when(this.shipmentRepository.findById(shipmentId())).thenReturn(shipment);
        when(this.returningServicePort.findReturnByShipmentId(shipmentId())).thenReturn(Optional.of(details));

        final ShipmentRouteLog result = this.shipmentPort.loadShipmentWithRouteLog(shipmentId());

        assertEquals(shipment.snapshot(), result.shipment().snapshot());
        assertSame(details, result.returnPackage());
    }

    @Test
    void shouldIncludeReturnInShipmentDetailsByTrackingNumber() {
        final TrackingNumber trackingNumber = new TrackingNumber("MGR-10");
        final Shipment shipment = returnedShipment();
        final ReturnDetailsDto details = returnDetails(new ReturnId(123L));
        when(this.shipmentRepository.findByTrackingNumber(trackingNumber)).thenReturn(shipment);
        when(this.returningServicePort.findReturnByShipmentId(shipmentId())).thenReturn(Optional.of(details));

        final ShipmentRouteLog result = this.shipmentPort.loadShipmentWithRouteLog(trackingNumber);

        assertSame(details, result.returnPackage());
        verify(this.returningServicePort).findReturnByShipmentId(shipmentId());
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
        when(this.departmentServicePort.getDepartmentId(destination)).thenReturn(new DepartmentId(12L));

        this.shipmentPort.changeDestination(shipmentId(), destination);

        assertEquals(new DepartmentId(12L), shipment.getTargetDepartmentId());
        verify(this.shipmentRepository).createOrUpdate(shipment);
        verify(this.domainEventPublisher).publish(
                any(ShipmentDestinationChanged.class));
    }

    private ShipmentCreateCommand shipmentCreateCommand() {
        final ShipmentCreateCommand command = new ShipmentCreateCommand(
                null,
                DataTestCreator.money(),
                recipient(),
                sender(),
                CountryCode.PL,
                CountryCode.DE,
                ShipmentPriority.MEDIUM
        );
        command.setDimensions(new Dimensions(new java.math.BigDecimal("20"), new java.math.BigDecimal("20"),
                new java.math.BigDecimal("20"), LengthUnit.CM));
        command.setWeight(new Weight(new java.math.BigDecimal("5"), WeightUnit.KG));
        return command;
    }

    private Shipment plannedShipment() {
        return new Shipment(
                shipmentId(),
                sender(),
                recipient(),
                null,
                DataTestCreator.money(),
                false,
                new DepartmentId(10L),
                new DepartmentId(9L),
                null,
                ShipmentPriority.MEDIUM,
                new TrackingNumber("PLANNED-TRACKING-NUMBER"),
                ShipmentStatus.PLANNED,
                null,
                PickupMethod.LOCKER,
                DeliveryMethod.COURIER,
                new PickupPointId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
        );
    }

    private Shipment returnedShipment() {
        final Shipment shipment = shipment();
        shipment.markAsDelivered();
        shipment.notifyShipmentReturned();
        return shipment;
    }

    private ReturnDetailsDto returnDetails(final ReturnId returnId) {
        return new ReturnDetailsDto(new LongValueDto(returnId.getId()), new LongValueDto(shipmentId().getValue()),
                "Damaged package", com.warehouse.commonassets.enumeration.ReturnStatus.CREATED,
                new StringValueDto("TOKEN"), new DepartmentId(1L), new DepartmentId(1L),
                new StringValueDto("KT1"), new StringValueDto("KT1"), new LongValueDto(1L), new LongValueDto(2L),
                new StringValueDto("DAMAGED"), new OperatorId(77L), null, null);
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


    @Test
    void shouldCancelLinkedReturnShipmentAndPublishReadModelChangeOnce() {
        final Shipment shipment = shipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentPort.notifyShipmentReturnCanceled(shipmentId());

        assertEquals(ShipmentStatus.CANCELED, shipment.getShipmentStatus());
        assertTrue(shipment.getLocked());
        verify(shipmentRepository, times(1)).createOrUpdate(shipment);
        verify(domainEventPublisher, times(1)).publish(any(com.warehouse.shipment.domain.event.ShipmentCanceled.class));
    }

    @Test
    void shouldRestoreReturnedShipmentAndPublishReadModelChangeOnce() {
        final Shipment shipment = returnedShipment();
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        shipmentPort.restoreAfterReturnCancellation(shipmentId());

        assertEquals(ShipmentStatus.DELIVERY, shipment.getShipmentStatus());
        verify(shipmentRepository, times(1)).createOrUpdate(shipment);
        verify(domainEventPublisher, times(1)).publish(any(com.warehouse.shipment.domain.event.ShipmentStatusChanged.class));
    }
}
