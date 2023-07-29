package com.warehouse.shipment;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.shipment.configuration.ShipmentTestConfiguration;
import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShipmentIntegrationTest {

    @Autowired
    private ShipmentPort shipmentPort;

    @Mock
    private PathFinderServicePort pathFinderServicePort;

    @Mock
    private ShipmentService shipmentService;


    @Test
    @DatabaseSetup("/database/db.xml")
    @Disabled
    void shouldShipParcel() {
        // given
        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(createParcel())
                .build();
        // when
        final ShipmentResponse response = shipmentPort.ship(request);
        // then
        assertThat(response.getParcelId()).isEqualTo(1L);
    }

    @Test
    void shouldNotShipWhenThereIsNoParcelInRequest() {
        // given
        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(null)
                .build();
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expectedToBe("Parcel not found in request"), exception.getMessage());
    }

    @Test
    @Disabled
    void shouldNotShipParcelWhenPathFinderServiceIsNotAvailable() {
        // given
        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(createParcel())
                .build();
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final MalformedURLException exception = assertThrows(MalformedURLException.class, executable);
        assertEquals(expectedToBe("Error registered"), exception.getMessage());
    }

    @Test
    void shouldNotDetermineNearestDepotForParcelDelivery() {
        // given
        final ShipmentParcel parcel = createParcel();

        final Recipient recipient = createRecipient();
        recipient.setCity("");

        // update recipient for test
        parcel.setRecipient(recipient);

        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(parcel)
                .build();
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final DestinationDepotDeterminationException exception =
                assertThrows(DestinationDepotDeterminationException.class, executable);
        assertEquals(expectedToBe("Delivery depot could not be determined"), exception.getMessage());
    }

    private ShipmentParcel createParcel() {
        return ShipmentParcel.builder()
                .parcelSize(Size.TEST)
                .price(99)
                .status(Status.CREATED)
                .sender(createSender())
                .recipient(createRecipient())
                .build();
    }

    private Recipient createRecipient() {
        return Recipient.builder()
                .firstName("test")
                .lastName("test")
                .city("Katowice")
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

    private String expectedToBe(String value) {
        return value;
    }

}
