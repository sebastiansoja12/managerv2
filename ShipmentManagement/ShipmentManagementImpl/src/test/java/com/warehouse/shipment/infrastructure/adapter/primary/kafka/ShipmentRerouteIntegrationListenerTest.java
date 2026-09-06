package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.reroute.api.event.ShipmentRerouteRequestedIntegrationEvent;
import com.warehouse.reroute.api.event.snapshot.ReroutePartyData;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentRerouteCommand;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import com.warehouse.shipment.domain.vo.ShipmentOwnerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentRerouteIntegrationListenerTest {

    private static final TrackingNumber TRACKING_NUMBER = new TrackingNumber("TRK-001");

    @Mock
    private ShipmentPort shipmentPort;

    private ShipmentRerouteIntegrationListener listener;

    @BeforeEach
    void setUp() {
        listener = new ShipmentRerouteIntegrationListener(shipmentPort, new OperatorContext());
    }

    @Test
    void shouldRerouteShipmentInOwnerOperatorContext() {
        when(shipmentPort.findContext(TRACKING_NUMBER)).thenReturn(new ShipmentOwnerContext(OperatorId.of(7L)));

        listener.handle(event());

        final ArgumentCaptor<ShipmentRerouteCommand> command = ArgumentCaptor.forClass(ShipmentRerouteCommand.class);
        verify(shipmentPort).reroute(command.capture());
        assertAll(
                () -> assertEquals(TRACKING_NUMBER, command.getValue().trackingNumber()),
                () -> assertEquals("Warszawa", command.getValue().recipient().getCity()),
                () -> assertEquals(CountryCode.PL, command.getValue().sender().getCountryCode()));
    }

    @Test
    void shouldSkipEventWhenShipmentDoesNotExist() {
        when(shipmentPort.findContext(TRACKING_NUMBER)).thenReturn(null);

        listener.handle(event());

        verify(shipmentPort, never()).reroute(any());
    }

    @Test
    void shouldNotRetryWhenShipmentCannotBeRerouted() {
        when(shipmentPort.findContext(TRACKING_NUMBER)).thenReturn(new ShipmentOwnerContext(OperatorId.of(7L)));
        when(shipmentPort.reroute(any())).thenThrow(new ShipmentModificationException("Shipment cannot be rerouted"));

        listener.handle(event());

        verify(shipmentPort).reroute(any());
    }

    @Test
    void shouldPropagateTechnicalFailureForRetry() {
        when(shipmentPort.findContext(TRACKING_NUMBER)).thenReturn(new ShipmentOwnerContext(OperatorId.of(7L)));
        when(shipmentPort.reroute(any())).thenThrow(new RestException(500, "Database unavailable"));

        assertThrows(RestException.class, () -> listener.handle(event()));
    }

    private ShipmentRerouteRequestedIntegrationEvent event() {
        return new ShipmentRerouteRequestedIntegrationEvent(1L, new ShipmentId(10L), TRACKING_NUMBER.value(),
                party("Kraków"), party("Warszawa"), Instant.parse("2026-10-17T10:00:00Z"));
    }

    private ReroutePartyData party(final String city) {
        return new ReroutePartyData("Jan", "Kowalski", "jan@example.com", "123456789", city, "00-001",
                "Prosta 1", "PL");
    }
}
