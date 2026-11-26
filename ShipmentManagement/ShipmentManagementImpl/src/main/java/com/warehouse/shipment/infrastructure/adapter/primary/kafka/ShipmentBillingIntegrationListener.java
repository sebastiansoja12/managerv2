package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.billing.api.event.PaymentExpiredIntegrationEvent;
import com.warehouse.billing.api.event.PriceQuotePaidIntegrationEvent;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShipmentBillingIntegrationListener {

    private final ShipmentPort shipmentPort;

    public ShipmentBillingIntegrationListener(final ShipmentPort shipmentPort) {
        this.shipmentPort = shipmentPort;
    }

    @IntegrationEventListener(group = "manager-shipment-billing")
    public void handle(final PriceQuotePaidIntegrationEvent event) {
        try {
            this.shipmentPort.markAsPaid(event.payload().priceQuoteId());
        } catch (final ShipmentModificationException exception) {
            log.warn("Shipment of price quote {} was not marked as paid: {}", event.payload().priceQuoteId().value(),
                    exception.getMessage());
        }
    }

    @IntegrationEventListener(group = "manager-shipment-billing")
    public void handle(final PaymentExpiredIntegrationEvent event) {
        try {
            this.shipmentPort.cancelForExpiredPayment(event.priceQuoteId());
        } catch (final ShipmentModificationException exception) {
            log.warn("Shipment of expired price quote {} was not canceled: {}", event.priceQuoteId().value(),
                    exception.getMessage());
        }
    }
}
