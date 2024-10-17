package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.ShipmentServiceImpl;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.City;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceImplTest {

    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private PathFinderServicePort pathFinderServicePort;

    @Mock
    private NotificationCreatorProvider notificationCreatorProvider;
    @Mock
    private MailServicePort mailServicePort;
    @Mock
    private Logger logger;

    @Mock
    private RouteLogServicePort routeLogServicePort;

    @Mock
    SoftwareConfigurationServicePort softwareConfigurationServicePort;

    private ShipmentServiceImpl service;

	@BeforeEach
	void setup() {
		service = new ShipmentServiceImpl(shipmentRepository, routeLogServicePort, softwareConfigurationServicePort);
	}

    @Test
    void shouldCreateShip() {
        // given
        final Shipment shipment = createShipmentParcel();

        final City city = new City("Katowice");

        final Parcel parcel = createParcel();

        final ShipmentId shipmentId = shipmentId();
        final RouteProcess routeProcess = RouteProcess.from(shipmentId, UUID.randomUUID());

        final Notification notification = new Notification("test",
                "test@test.pl", "test");

        doReturn(city)
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));

        doReturn(parcel)
                .when(shipmentRepository)
                .save(shipment);

        doReturn(notification)
                .when(notificationCreatorProvider)
                .createNotification(any());

        doNothing()
                .when(mailServicePort)
                .sendShipmentNotification(notification);

        when(routeLogServicePort.initializeRouteProcess(shipmentId, null)).thenReturn(routeProcess);


        // when
        service.createShipment(shipment);
        // then

    }


    @Test
    void shouldNotCreateShipmentWhenDestinationDepotIsNotDetermined() {
        // given
        final Shipment shipment = createShipmentParcel();

        doReturn(new City(null))
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));
        // when
        final Executable executable = () -> service.createShipment(shipment);
        // then
        final DestinationDepotDeterminationException exception =
                assertThrows(DestinationDepotDeterminationException.class, executable);

        assertEquals(exception.getMessage(),
                expectedToBe("Delivery depot could not be determined"));
    }

    @Test
    void shouldFind() {
        // given
        final ShipmentId shipmentId = shipmentId();

        // parcel with id 1L
        final Parcel expectedParcel = createParcel();


        // when

        // then

    }

    @Test
    void shouldNotFindAndThrowException() {
        // given
        final ShipmentId shipmentId = shipmentId();

        // parcel with id 1L
        final Parcel expectedParcel = createParcel();

        // build exception to throw
        final ShipmentNotFoundException expectedException = new ShipmentNotFoundException("Parcel was not found");


        // when
        final Executable executable = () -> service.find(shipmentId);

        // then
        final ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class, executable);
        assertEquals(exception.getMessage(), expectedToBe("Parcel was not found"));
    }

    @Test
    void shouldUpdateShipmentParcelWithUpdatingDeliveryDepotWhenItsChanged() {
        // given
        final ShipmentUpdate shipmentUpdate = mock(ShipmentUpdate.class);

        final Parcel parcel = createParcel();

        final City city = new City("KT1");

        doReturn(city)
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));

        // when

        // then
    }

    @Test
    void shouldUpdateParcelAndDontUpdateShipmentDeliveryDepotWhenItWasNotChanged() {
        // given

        final Parcel parcel = createParcel();

        final City city = new City("KT1");

        doReturn(city)
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));


        // when
        //final UpdateParcelResponse response = service.update(shipmentUpdate);
        // then

    }

    @Test
    void shouldExist() {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);

        doReturn(true)
                .when(shipmentRepository)
                .exists(shipmentId);

        // when
        final boolean doesExist = service.existsShipment(shipmentId);
        // then
        assertEquals(expectedToBe(true), doesExist);
    }

    @Test
    void shouldNotExist() {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);

        doReturn(false)
                .when(shipmentRepository)
                .exists(shipmentId);

        // when
        final boolean doesExist = service.existsShipment(shipmentId);
        // then
        assertEquals(expectedToBe(false), doesExist);
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

}
