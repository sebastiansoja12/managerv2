package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.shipment;
import static com.warehouse.shipment.DataTestCreator.shipmentId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import com.google.common.collect.Sets;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.handler.*;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentCreateCommand;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.registry.DomainContext;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.domain.vo.ChangeShipmentTypeRequest;
import com.warehouse.shipment.domain.vo.ShipmentCreateResponse;
import com.warehouse.shipment.domain.vo.ShipmentStatusRequest;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;

@ExtendWith(MockitoExtension.class)
class ShipmentPortImplTest {

    @Mock
    private PathFinderServicePort pathFinderServicePort;

    @Mock
    private RouteLogServicePort routeLogServicePort;

    @Mock
    private ShipmentRepository shipmentRepository;
    
    @Mock
    private SoftwareConfigurationServicePort softwareConfigurationServicePort;

    @Mock
    private CountryDetermineServicePort countryDetermineServicePort;

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private SignatureRepository signatureRepository;

    @Mock
    private ReturningServicePort returningServicePort;

    @Mock
    private MailNotificationServicePort mailNotificationServicePort;

    private Set<ShipmentStatusHandler> shipmentStatusHandlers;

    private ShipmentPortImpl shipmentPort;

    private static final String SHIPMENT_WAS_NOT_FOUND = "Shipment not found";

    @BeforeEach
    void setUp() {
        final ShipmentService shipmentService = new ShipmentServiceImpl(shipmentRepository);
        final CountryDetermineService countryDetermineService = new CountryDetermineServiceImpl(countryDetermineServicePort, countryRepository);
        final PriceService priceService = new PriceServiceImpl(priceRepository);
        final NotificationCreatorProvider notificationCreatorProvider = new NotificationCreatorProviderImpl();
        final CountryServiceAvailabilityService countryServiceAvailabilityService =
                new CountryServiceAvailabilityServiceImpl(departmentRepository);
        final SignatureService signatureService = new SignatureServiceImpl(signatureRepository, shipmentRepository);
        final Logger logger = mock(Logger.class);
        shipmentStatusHandlers = Sets.newHashSet(Sets.newHashSet(
                new ShipmentCreatedHandler(), new ShipmentRerouteHandler(shipmentService),
                new ShipmentSentHandler(shipmentService), new ShipmentDeliveryHandler(shipmentService),
                new ShipmentRedirectHandler(shipmentService), new ShipmentReturnHandler(shipmentService)));
        final ApplicationContext applicationContext = mock(ApplicationContext.class);
        final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        final DomainContext domainContext = new DomainContext();
        domainContext.setApplicationEventPublisher(eventPublisher);
        domainContext.setApplicationContext(applicationContext);
        final TrackingNumberService trackingNumberService = new TrackingNumberServiceImpl(Set.of());
		shipmentPort = new ShipmentPortImpl(shipmentService, logger, pathFinderServicePort, notificationCreatorProvider,
				shipmentStatusHandlers, countryDetermineService, priceService, countryServiceAvailabilityService,
				signatureService, routeLogServicePort, returningServicePort, mailNotificationServicePort,
                trackingNumberService);
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
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.RETURN);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        shipmentPort.changeShipmentStatusTo(request);
        assertEquals(ShipmentStatus.RETURN, shipment.getShipmentStatus());
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
        when(shipmentRepository.findById(shipmentId)).thenReturn(null);
        final Executable executable = () -> shipmentPort.changeSenderTo(shipmentId, any());
        final RestException exception = assertThrows(RestException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeRecipientToWhenShipmentWasNotFound() {
        final ShipmentId shipmentId = shipmentId();
        when(shipmentRepository.findById(shipmentId)).thenReturn(null);
        final Executable executable = () -> shipmentPort.changeRecipientTo(shipmentId, any());
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
