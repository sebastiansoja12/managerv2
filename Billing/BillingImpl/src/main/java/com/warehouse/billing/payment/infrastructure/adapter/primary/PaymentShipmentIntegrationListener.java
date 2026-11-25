package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentIntegrationEventMapper;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.shipment.api.event.ShipmentCanceledIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentShipmentIntegrationListener {

    private final PaymentPort paymentPort;

    private final PaymentIntegrationEventMapper paymentIntegrationEventMapper;

    public PaymentShipmentIntegrationListener(final PaymentPort paymentPort,
                                              final PaymentIntegrationEventMapper paymentIntegrationEventMapper) {
        this.paymentPort = paymentPort;
        this.paymentIntegrationEventMapper = paymentIntegrationEventMapper;
    }

    @IntegrationEventListener(group = "manager-billing-payment")
    public void handle(final ShipmentCreatedIntegrationEvent event) {
        this.paymentPort.open(this.paymentIntegrationEventMapper.toCommand(event));
    }

    @IntegrationEventListener(group = "manager-billing-payment")
    public void handle(final ShipmentCanceledIntegrationEvent event) {
        try {
            this.paymentIntegrationEventMapper.toPriceQuoteId(event).ifPresent(priceQuoteId ->
                    this.paymentPort.handleShipmentCanceled(priceQuoteId, event.previousStatus()));
        } catch (final PaymentException exception) {
            if (PaymentException.Reason.GATEWAY_FAILURE.equals(exception.reason())) {
                throw exception;
            }
            log.warn("Shipment cancellation left the payment unchanged: {}", exception.getMessage());
        }
    }
}
