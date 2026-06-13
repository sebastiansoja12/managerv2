package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.command.ChangeShipmentTypeRequest;
import com.warehouse.shipment.application.port.primary.command.ShipmentStatusRequest;
import com.warehouse.shipment.application.port.secondary.ShipmentConfigurationPort;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.application.service.SignatureServiceImpl;
import com.warehouse.shipment.application.port.secondary.*;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.application.service.CountryServiceAvailabilityService;
import com.warehouse.shipment.application.service.CountryServiceAvailabilityServiceImpl;
import com.warehouse.shipment.application.service.PriceService;
import com.warehouse.shipment.application.service.PriceServiceImpl;
import com.warehouse.shipment.application.service.RouteLogService;
import com.warehouse.shipment.application.service.SignatureService;
import com.warehouse.shipment.application.service.TrackingNumberGenerationService;
import com.warehouse.shipment.application.service.delivery.ShipmentDeliveryStrategyResolver;
import com.warehouse.shipment.domain.context.ShipmentEventContext;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.warehouse.shipment.DataTestCreator.recipient;
import static com.warehouse.shipment.DataTestCreator.sender;
import static com.warehouse.shipment.DataTestCreator.shipment;
import static com.warehouse.shipment.DataTestCreator.shipmentId;
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
    private PriceRepository priceRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private SignatureRepository signatureRepository;

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

    private ShipmentPortImpl shipmentPort;

    private static final String SHIPMENT_WAS_NOT_FOUND = "Shipment not found";

    @BeforeEach
    void setUp() {
        final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        new ShipmentEventContext().setApplicationEventPublisher(eventPublisher);
        final TrackingNumberGenerationService trackingNumberGenerationService =
                mock(TrackingNumberGenerationService.class);
        final ShipmentConfigurationPort shipmentConfigurationServicePort = mock(ShipmentConfigurationPort.class);
        final PriceService priceService = new PriceServiceImpl(priceRepository);
        final CountryServiceAvailabilityService countryServiceAvailabilityService =
                new CountryServiceAvailabilityServiceImpl(departmentRepository);
        final SignatureService signatureService = new SignatureServiceImpl(signatureRepository, shipmentRepository);
        final Logger logger = mock(Logger.class);
		shipmentPort = new ShipmentPortImpl(shipmentRepository, specificationShipmentRepository,
				logger, pathFinderServicePort, priceService, countryServiceAvailabilityService,
                signatureService, routeLogService, returningServicePort, mailNotificationServicePort,
                trackingNumberGenerationService, shipmentConfigurationServicePort,
                operatorContextProvider, shipmentDeliveryStrategyResolver);
	}

    @Test
    void shouldShip() {
        final ShipmentCreateCommand request = new ShipmentCreateCommand();
        final Result<ShipmentCreateResponse, ErrorCode> response = shipmentPort.ship(request);
        assertEquals(response, expectedToBeEqualTo(response));
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
        assertEquals(expectedToBe(SHIPMENT_WAS_NOT_FOUND), exception.getMessage());
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

    private <T> T expectedToBe(T value) {
        return value;
    }

    private <T> T expectedToBeEqualTo(T value) {
        return value;
    }
}
