package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.createShipment;
import static com.warehouse.shipment.DataTestCreator.shipmentId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.DestinationDepartmentDeterminationException;
import com.warehouse.shipment.domain.exception.ShipmentEmptyRequestException;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.service.ShipmentServiceImpl;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;

@ExtendWith(MockitoExtension.class)
class ShipmentPortImplTest {

    @Mock
    private PathFinderServicePort pathFinderServicePort;

    @Mock
    private NotificationCreatorProvider notificationCreatorProvider;

    @Mock
    private MailServicePort mailServicePort;

    @Mock
    private RouteLogServicePort routeLogServicePort;

    @Mock
    private ShipmentRepository shipmentRepository;
    
    @Mock
    private SoftwareConfigurationServicePort softwareConfigurationServicePort;

    @Mock
    private TrackingStatusServicePort trackingStatusServicePort;

    private ShipmentPortImpl shipmentPort;

    private final static UUID processId = UUID.fromString("2d255296-3f50-4cc1-b8dc-ef6e634aab0d");

    private static final String SHIPMENT_WAS_NOT_FOUND = "Shipment was not found";

    @BeforeEach
    void setUp() {
        final ShipmentService shipmentService = new ShipmentServiceImpl(shipmentRepository, routeLogServicePort,
                softwareConfigurationServicePort);
        final Logger logger = mock(Logger.class);
        shipmentPort = new ShipmentPortImpl(shipmentService, logger, pathFinderServicePort, notificationCreatorProvider,
                mailServicePort, trackingStatusServicePort);
    }

    @Test
    void shouldShip() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        final City city = new City("KT1");
        final RouteProcess routeProcess = new RouteProcess(shipmentId, processId);
        final SoftwareConfiguration softwareConfiguration = new SoftwareConfiguration("value", "url");

        doReturn(city)
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));

        doReturn(softwareConfiguration)
                .when(softwareConfigurationServicePort)
                .getSoftwareConfiguration();

        when(routeLogServicePort.initializeRouteProcess(any(), any())).thenReturn(routeProcess);
        // when
        final ShipmentResponse response = shipmentPort.ship(request);
        // then
        assertEquals(response, expectedToBeEqualTo(response));
    }

    @Test
    void shouldNotShipWhenRequestIsEmpty() {
        // given
        final ShipmentRequest request = new ShipmentRequest(null);
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final ShipmentEmptyRequestException exception = assertThrows(ShipmentEmptyRequestException.class, executable);
        assertEquals("Shipment not found in request", exception.getMessage());
    }

    @Test
    void shouldThrowDestinationDepotDeterminationExceptionWhenCityIsNull() {
        // given
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        doReturn(null)
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
		final DestinationDepartmentDeterminationException exception = assertThrows(
				DestinationDepartmentDeterminationException.class, executable);
        assertEquals("Delivery department could not be determined", exception.getMessage());
    }

    @Test
    void shouldThrowDestinationDepotDeterminationExceptionWhenCityValueIsEmpty() {
        // given
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        final City city = new City("");
        doReturn(city)
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
		final DestinationDepartmentDeterminationException exception = assertThrows(
				DestinationDepartmentDeterminationException.class, executable);
        assertEquals("Delivery department could not be determined", exception.getMessage());
    }

    @Test
    void shouldChangeSenderTo() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = mock(Shipment.class);
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        shipmentPort.changeSenderTo(request);
        // then
        verify(shipmentRepository).findById(shipmentId);
        verify(shipmentRepository).update(shipment);
    }

    @Test
    void shouldChangeRecipientTo() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = mock(Shipment.class);
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        shipmentPort.changeRecipientTo(request);
        // then
        verify(shipmentRepository).findById(shipmentId);
        verify(shipmentRepository).update(shipment);
    }

    @Test
    void shouldChangeShipmentTypeTo() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = mock(Shipment.class);
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        shipmentPort.changeShipmentTypeTo(request);
        // then
        verify(shipmentRepository).findById(shipmentId);
        verify(shipmentRepository).update(shipment);
    }

    @Test
    void shouldChangeShipmentStatusToRedirected() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = mock(Shipment.class);
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.REDIRECT);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        shipmentPort.changeShipmentStatusTo(request);
        // then
        verify(shipment).notifyRelatedShipmentRedirected(any());
    }

    @Test
    void shouldChangeShipmentStatusToRerouted() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = mock(Shipment.class);
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.REROUTE);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        shipmentPort.changeShipmentStatusTo(request);
        // then
        verify(shipment).notifyShipmentRerouted();
    }

    @Test
    void shouldChangeShipmentStatusToSent() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = mock(Shipment.class);
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.SENT);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        shipmentPort.changeShipmentStatusTo(request);
        // then
        verify(shipment).notifyShipmentSent();
    }

    @Test
    void shouldChangeShipmentStatusToReturned() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = mock(Shipment.class);
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.RETURN);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        shipmentPort.changeShipmentStatusTo(request);
        // then
        verify(shipment).notifyShipmentReturned();
    }

    @Test
    void shouldChangeShipmentStatusToDelivered() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final Shipment shipment = mock(Shipment.class);
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.DELIVERY);
        doReturn(shipment)
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        shipmentPort.changeShipmentStatusTo(request);
        // then
        verify(shipment).notifyShipmentDelivered();
    }

    @Test
    void shouldTryChangeShipmentStatusToCreatedAndThrowException() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.CREATED);
        // when
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals("Shipment already created, status cannot be changed", exception.getMessage());
    }

    @Test
    void shouldNotChangeSenderToWhenShipmentWasNotFound() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.changeSenderTo(request);
        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeRecipientToWhenShipmentWasNotFound() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.changeRecipientTo(request);
        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentTypeToWhenShipmentWasNotFound() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentRequest request = new ShipmentRequest(createShipment());
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.changeShipmentTypeTo(request);
        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToRedirect() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.REDIRECT);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToRerouted() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.REROUTE);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToSent() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.SENT);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToReturned() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.RETURN);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldNotChangeShipmentStatusToDelivered() {
        // given
        final ShipmentId shipmentId = shipmentId();
        final ShipmentStatusRequest request = new ShipmentStatusRequest(shipmentId, ShipmentStatus.DELIVERY);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.changeShipmentStatusTo(request);
        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(SHIPMENT_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldLoadShipment() {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);
        final Shipment expectedShipment = mock(Shipment.class);
        when(shipmentRepository.findById(shipmentId)).thenReturn(expectedShipment);
        // when
        final Shipment shipment = shipmentPort.loadShipment(shipmentId);
        // then
        assertEquals(shipment, expectedShipment);
    }

    @Test
    void shouldNotLoadShipment() {
        // given
        final ShipmentId shipmentId = new ShipmentId(0L);
        doThrow(new ShipmentNotFoundException(SHIPMENT_WAS_NOT_FOUND))
                .when(shipmentRepository)
                .findById(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.loadShipment(shipmentId);
        // then
        final ShipmentNotFoundException exception =
                assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(expectedToBe(SHIPMENT_WAS_NOT_FOUND), exception.getMessage());
    }

    @Test
    void shouldCheckIfShipmentExists() {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);
        when(shipmentRepository.exists(shipmentId)).thenReturn(true);
        // when
        final boolean exists = shipmentPort.existsShipment(shipmentId);
        // then
        assertTrue(exists);
    }

    @Test
    void shouldCheckIfShipmentNotExists() {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);
        when(shipmentRepository.exists(shipmentId)).thenReturn(false);
        // when
        final boolean exists = shipmentPort.existsShipment(shipmentId);
        // then
        assertFalse(exists);
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

    private <T> T expectedToBeEqualTo(T value) {
        return value;
    }
}
