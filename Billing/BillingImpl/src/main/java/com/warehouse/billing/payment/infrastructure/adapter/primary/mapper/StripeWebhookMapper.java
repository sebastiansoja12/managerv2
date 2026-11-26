package com.warehouse.billing.payment.infrastructure.adapter.primary.mapper;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmGatewayRefundCommand;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.infrastructure.adapter.primary.exception.InvalidWebhookPayloadException;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.WebhookEventId;

import java.util.Optional;
import java.util.Set;

public class StripeWebhookMapper {

    private static final Set<String> CHECKOUT_PAYMENT_EVENTS = Set.of(
            "checkout.session.completed",
            "checkout.session.async_payment_succeeded"
    );

    private static final String CHARGE_REFUNDED_EVENT = "charge.refunded";

    public Optional<ConfirmOnlinePaymentCommand> toConfirmation(final Event event) {
        if (!CHECKOUT_PAYMENT_EVENTS.contains(event.getType())) {
            return Optional.empty();
        }
        try {
            final StripeObject object = event.getDataObjectDeserializer().deserializeUnsafe();
            return Optional.of(object)
                    .filter(Session.class::isInstance)
                    .map(Session.class::cast)
                    .filter(session -> "paid".equals(session.getPaymentStatus()))
                    .map(session -> new ConfirmOnlinePaymentCommand(
                            Optional.ofNullable(session.getMetadata())
                                    .map(metadata -> metadata.get("paymentId"))
                                    .map(value -> new PaymentId(Long.valueOf(value)))
                                    .orElse(null),
                            session.getId(),
                            session.getPaymentIntent(),
                            new WebhookEventId(event.getId())));
        } catch (final EventDataObjectDeserializationException | NumberFormatException exception) {
            throw new InvalidWebhookPayloadException("Checkout session of the webhook event cannot be read");
        }
    }

    public Optional<ConfirmGatewayRefundCommand> toRefundConfirmation(final Event event) {
        if (!CHARGE_REFUNDED_EVENT.equals(event.getType())) {
            return Optional.empty();
        }
        try {
            final StripeObject object = event.getDataObjectDeserializer().deserializeUnsafe();
            return Optional.of(object)
                    .filter(Charge.class::isInstance)
                    .map(Charge.class::cast)
                    .filter(charge -> Boolean.TRUE.equals(charge.getRefunded()) && charge.getPaymentIntent() != null)
                    .map(charge -> new ConfirmGatewayRefundCommand(charge.getPaymentIntent(),
                            new WebhookEventId(event.getId())));
        } catch (final EventDataObjectDeserializationException exception) {
            throw new InvalidWebhookPayloadException("Refunded charge of the webhook event cannot be read");
        }
    }
}
