package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.billing.api.event.PaymentExpiredIntegrationEvent;
import com.warehouse.billing.api.event.PriceQuotePaidIntegrationEvent;
import com.warehouse.billing.api.event.snapshot.PriceQuotePaidData;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ShipmentBillingIntegrationListenerTest {

    private static final PriceQuoteId PRICE_QUOTE_ID = new PriceQuoteId(55L);

    private ShipmentPort shipmentPort;

    private ShipmentBillingIntegrationListener listener;

    @BeforeEach
    void setUp() {
        this.shipmentPort = mock(ShipmentPort.class);
        this.listener = new ShipmentBillingIntegrationListener(this.shipmentPort);
    }

    @Test
    void shouldMarkShipmentPaidOnPriceQuotePaidEvent() {
        this.listener.handle(event());

        verify(this.shipmentPort).markAsPaid(PRICE_QUOTE_ID);
    }

    @Test
    void shouldSkipShipmentThatDoesNotRequirePayment() {
        doThrow(new ShipmentModificationException("Shipment does not require payment"))
                .when(this.shipmentPort).markAsPaid(PRICE_QUOTE_ID);

        assertDoesNotThrow(() -> this.listener.handle(event()));
    }

    @Test
    void shouldCancelShipmentOnPaymentExpired() {
        this.listener.handle(expiredEvent());

        verify(this.shipmentPort).cancelForExpiredPayment(PRICE_QUOTE_ID);
    }

    @Test
    void shouldSkipShipmentThatCannotBeCanceledForExpiredPayment() {
        doThrow(new ShipmentModificationException("Shipment cannot be cancelled for expired payment"))
                .when(this.shipmentPort).cancelForExpiredPayment(PRICE_QUOTE_ID);

        assertDoesNotThrow(() -> this.listener.handle(expiredEvent()));
    }

    private PaymentExpiredIntegrationEvent expiredEvent() {
        return new PaymentExpiredIntegrationEvent(PRICE_QUOTE_ID, new PaymentId(1L),
                Instant.parse("2026-11-27T08:15:00Z"));
    }

    private PriceQuotePaidIntegrationEvent event() {
        return new PriceQuotePaidIntegrationEvent(new PriceQuotePaidData(PRICE_QUOTE_ID, new PaymentId(1L),
                Instant.parse("2026-11-24T12:00:00Z")));
    }
}
