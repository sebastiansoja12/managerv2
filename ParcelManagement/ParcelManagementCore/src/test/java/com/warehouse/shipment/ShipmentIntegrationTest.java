package com.warehouse.shipment;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import com.warehouse.paypal.domain.model.Link;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.shipment.configuration.ShipmentTestConfiguration;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.ShipmentPaymentException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.PaypalServicePort;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.voronoi.VoronoiService;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShipmentIntegrationTest {

    @Autowired
    private ShipmentPort shipmentPort;

    @Autowired
    private PathFinderServicePort pathFinderServicePort;

    @Mock
    private PaypalServicePort paypalServicePort;

    @Autowired
    private PaypalPort paypalPort;

    @Autowired
    private VoronoiService voronoiService;

    @Test
    void shouldShipParcel() {
        // given
        final ShipmentParcel shipmentParcel = createShipmentParcel();
        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(shipmentParcel)
                .build();
        when(paypalPort.payment(any())).thenReturn(PaymentResponse.builder()
                .failureReason("NONE")
                .link(new Link("link"))
                .paymentMethod("PAYPAL")
                .createTime("now")
                .build());
        // when
        final ShipmentResponse response = shipmentPort.ship(request);
        // then
        assertThat(response.getParcelId()).isEqualTo(1000000000L);
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
    void shouldNotShipParcelWhenPaymentIsNotAvailable() {
        // given
        final ShipmentParcel shipmentParcel = createShipmentParcel();
        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(shipmentParcel)
                .build();
        when(voronoiService.findFastestRoute(shipmentParcel.getDestination())).thenReturn("KT3");
        when(paypalPort.payment(any())).thenReturn(PaymentResponse.builder()
                .failureReason("NONE")
                .link(new Link(""))
                .paymentMethod("PAYPAL")
                .createTime("now")
                .build());
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final ShipmentPaymentException exception = assertThrows(ShipmentPaymentException.class, executable);
        assertEquals(expectedToBe("URL for payment has not been generated"), exception.getMessage());
    }

    private ShipmentParcel createShipmentParcel() {
        return ShipmentParcel.builder()
                .parcelSize(Size.TEST)
                .price(99)
                .status(Status.CREATED)
                .sender(createSender())
                .recipient(createRecipient())
                .build();
    }

    private Parcel createParcel() {
        return Parcel.builder()
                .parcelSize(Size.TEST)
                .price(99)
                .parcelStatus(Status.CREATED)
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

    private <T> T expectedToBe(T value) {
        return value;
    }

}
