package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ParcelNotFoundException;

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

    private ShipmentServiceImpl service;

	@BeforeEach
	void setup() {
		service = new ShipmentServiceImpl(shipmentRepository, pathFinderServicePort,
                notificationCreatorProvider, mailServicePort, logger, routeLogServicePort);
	}

    @Test
    void shouldCreateShip() {
        // given
        final Shipment shipment = createShipmentParcel();

        final City city = new City("Katowice");

        final Parcel parcel = createParcel();

        final ShipmentId shipmentId = parcelId();
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

        when(routeLogServicePort.initializeRouteProcess(shipmentId)).thenReturn(routeProcess);


        // when
        final ShipmentResponse response = service.createShipment(shipment);
        // then
        assertAll(
                () -> assertEquals(response.parcelId(), expectedToBe(1L)),
                () -> assertThat(response.routeProcessId()).isInstanceOf(String.class)
        );
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
    void shouldLoadParcel() {
        // given
        final ShipmentId shipmentId = parcelId();

        // parcel with id 1L
        final Parcel expectedParcel = createParcel();

        doReturn(expectedParcel)
                .when(shipmentRepository)
                .findParcelById(shipmentId);
        // when
        final Parcel parcel = service.loadParcel(shipmentId);

        // then
        assertEquals(parcel.getId(), expectedToBe(1L));
        verify(shipmentRepository, times(1)).findParcelById(shipmentId);
    }

    @Test
    void shouldNotLoadParcelAndThrowException() {
        // given
        final ShipmentId shipmentId = parcelId();

        // parcel with id 1L
        final Parcel expectedParcel = createParcel();

        // build exception to throw
        final ParcelNotFoundException expectedException = new ParcelNotFoundException("Parcel was not found");

        doThrow(expectedException)
                .when(shipmentRepository)
                .findParcelById(shipmentId);
        // when
        final Executable executable = () -> service.loadParcel(shipmentId);

        // then
        final ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(exception.getMessage(), expectedToBe("Parcel was not found"));
    }

    @Test
    void shouldUpdateParcelWithUpdatingDeliveryDepotWhenItsChanged() {
        // given
        final ShipmentUpdate shipmentUpdate = mock(ShipmentUpdate.class);

        final Parcel parcel = createParcel();

        final City city = new City("KT1");

        doReturn(city)
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));

        doReturn(parcel)
                .when(shipmentRepository)
                .update(shipmentUpdate);

        // when

        // then
    }

    @Test
    void shouldUpdateParcelAndDontUpdateDeliveryDepotWhenItWasNotChanged() {
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
        final boolean doesExist = service.exists(shipmentId);
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
        final boolean doesExist = service.exists(shipmentId);
        // then
        assertEquals(expectedToBe(false), doesExist);
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

}
