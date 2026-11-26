package com.warehouse.billing.payment.infrastructure.adapter.primary.mapper;

import com.warehouse.billing.payment.application.port.primary.command.OpenPaymentCommand;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.shipment.api.event.ShipmentCanceledIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.PaymentStateDto;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;

import java.util.Optional;

public class PaymentIntegrationEventMapper {

    public OpenPaymentCommand toCommand(final ShipmentCreatedIntegrationEvent event) {
        final ShipmentEventData payload = event.payload();
        return new OpenPaymentCommand(payload.priceQuoteId(),
                PaymentStateDto.UNPAID.equals(payload.paymentState()) && payload.priceQuoteId() != null);
    }

    public Optional<PriceQuoteId> toPriceQuoteId(final ShipmentCanceledIntegrationEvent event) {
        return Optional.ofNullable(event.payload().priceQuoteId());
    }
}
