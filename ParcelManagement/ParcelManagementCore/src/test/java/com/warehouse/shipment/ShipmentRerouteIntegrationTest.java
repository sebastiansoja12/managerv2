package com.warehouse.shipment;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.shipment.configuration.ShipmentTestConfiguration;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentReroutePort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockService;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/shipment.xml")
public class ShipmentRerouteIntegrationTest {


    @Autowired
    private ShipmentReroutePort shipmentReroutePort;


    @Test
    void shouldUpdateParcel() {
        // given
        final UpdateParcelRequest request = UpdateParcelRequest.builder()
                .parcel(createParcel())
                .build();
        // when
        final UpdateParcelResponse response = shipmentReroutePort.reroute(request);
        // then
        assertAll(
                () -> assertNotEquals(expected(request.getParcel().getDestination()),
                        response.getParcel().getDestination()),
                // parcel is updated = request and response are equal
                () -> assertEquals(expected(request.getParcel().getRecipient().getTelephoneNumber()),
                        response.getParcel().getRecipient().getTelephoneNumber()),
                // parcel related id should be null
                () -> assertEquals(expected(request.getParcel().getParcelRelatedId()),
                        response.getParcel().getParcelRelatedId())

        );
    }

    @Test
    void shouldThrowParcelNotFoundException() {
        // given
        final UpdateParcelRequest request = UpdateParcelRequest.builder().build();
        // when
        final Executable executable = () -> shipmentReroutePort.reroute(request);
        // then
        final ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expected("Parcel not found in request"), exception.getMessage());
        assertEquals(expected(204), exception.getCode());
    }

    private <T> T expected(T value) {
        return value;
    }

    private Parcel createParcel() {
        return Parcel.builder()
                .id(100001L)
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

}
