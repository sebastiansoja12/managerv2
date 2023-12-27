package com.warehouse.shipment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.warehouse.shipment.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.port.primary.ShipmentReroutePortImpl;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.service.ShipmentServiceImpl;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.UpdateParcelRequest;
import com.warehouse.shipment.domain.vo.UpdateParcelResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;

@ExtendWith(MockitoExtension.class)
public class ShipmentReroutePortImplTest {
    

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

    private ShipmentReroutePortImpl shipmentReroutePort;

	@BeforeEach
	void setup() {
		final ShipmentService service = new ShipmentServiceImpl(shipmentRepository, pathFinderServicePort,
				notificationCreatorProvider, mailServicePort, logger, routeLogServicePort);
		shipmentReroutePort = new ShipmentReroutePortImpl(service);
    }

    @Test
    void shouldUpdateParcel() {
        // given
        final UpdateParcelRequest updateParcelRequest = UpdateParcelRequest
                .builder()
                .parcel(createParcel())
                .build();

        final Parcel parcel = createParcel();
        parcel.setParcelStatus(Status.REROUTE);

        final UpdateParcelResponse expectedResponse = new UpdateParcelResponse(parcel);

        when(pathFinderServicePort.determineDeliveryDepot(any())).thenReturn(new City("KT3"));
        when(shipmentRepository.update(any())).thenReturn(parcel);
        // when
        final UpdateParcelResponse response = shipmentReroutePort.reroute(updateParcelRequest);

        // then
        assertEquals(expectedResponse, response);

        // and status changed to reroute
        assertThat(response.getParcel().getParcelStatus().name()).isEqualTo("REROUTE");
    }

    @Test
    void shouldThrowExceptionWhenParcelIsNull() {
        // given
        final UpdateParcelRequest updateParcelRequest = UpdateParcelRequest.builder().build();
        // when, then
        assertThrows(ParcelNotFoundException.class, () -> shipmentReroutePort.reroute(updateParcelRequest));
    }


    private Parcel createParcel() {
        return Parcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .parcelStatus(Status.CREATED)
                .destination("KT1")
                .id(1L)
                .build();
    }

    private ShipmentParcel createShipmentParcel() {
        return ShipmentParcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .status(Status.CREATED)
                .destination("KT1")
                .build();
    }

    private Recipient createRecipient() {
        return Recipient.builder()
                .firstName("test")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

    private Sender createSender() {
        return Sender.builder()
                .firstName("updatedTest")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

    private ShipmentResponse expectedToBeEqualTo(ShipmentResponse response) {
        return response;
    }
}
