package com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway;

import com.stripe.StripeClient;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.RefundCreateParams;
import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.domain.enumeration.CheckoutClosure;
import com.warehouse.billing.payment.domain.enumeration.RefundOutcome;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.domain.vo.GatewayRefund;
import com.warehouse.commonassets.identificator.PaymentId;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;

@Slf4j
public class StripePaymentGatewayClient implements PaymentGatewayServicePort {

    private final StripeClient stripeClient;

    private final StripeCheckoutSessionParamsFactory checkoutSessionParamsFactory;

    private final Clock clock;

    public StripePaymentGatewayClient(final StripeClient stripeClient,
                                      final StripeCheckoutSessionParamsFactory checkoutSessionParamsFactory,
                                      final Clock clock) {
        this.stripeClient = stripeClient;
        this.checkoutSessionParamsFactory = checkoutSessionParamsFactory;
        this.clock = clock;
    }

    @Override
    public CheckoutSession openCheckout(final Payment payment) {
        final Instant now = this.clock.instant();
        try {
            final Session session = this.stripeClient.checkout().sessions().create(
                    this.checkoutSessionParamsFactory.create(payment, now),
                    RequestOptions.builder()
                            .setIdempotencyKey(this.checkoutSessionParamsFactory.idempotencyKey(payment, now))
                            .build());
            log.info("Stripe checkout session {} opened for payment {}", session.getId(), payment.id().value());
            return new CheckoutSession(session.getId(), session.getUrl(),
                    Instant.ofEpochSecond(session.getExpiresAt()));
        } catch (final StripeException exception) {
            log.warn("Stripe checkout for payment {} failed with code {} (request {})", payment.id().value(),
                    exception.getCode(), exception.getRequestId());
            throw new PaymentException(PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable");
        }
    }

    @Override
    public CheckoutClosure expireCheckout(final String gatewayReference) {
        try {
            final Session session = this.stripeClient.checkout().sessions().retrieve(gatewayReference);
            if ("complete".equals(session.getStatus()) && "paid".equals(session.getPaymentStatus())) {
                log.info("Stripe checkout session {} is already completed", gatewayReference);
                return CheckoutClosure.COMPLETED;
            }
            if ("open".equals(session.getStatus())) {
                this.stripeClient.checkout().sessions().expire(gatewayReference);
                log.info("Stripe checkout session {} expired", gatewayReference);
            }
            return CheckoutClosure.EXPIRED;
        } catch (final InvalidRequestException exception) {
            if ("resource_missing".equals(exception.getCode())) {
                log.warn("Stripe checkout session {} does not exist, treating it as expired", gatewayReference);
                return CheckoutClosure.EXPIRED;
            }
            log.warn("Stripe checkout session {} could not be expired, code {} (request {})", gatewayReference,
                    exception.getCode(), exception.getRequestId());
            throw new PaymentException(PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable");
        } catch (final StripeException exception) {
            log.warn("Stripe checkout session {} could not be expired, code {} (request {})", gatewayReference,
                    exception.getCode(), exception.getRequestId());
            throw new PaymentException(PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable");
        }
    }

    @Override
    public GatewayRefund refund(final Payment payment) {
        final String gatewayReference = payment.checkoutSession().map(CheckoutSession::gatewayReference)
                .orElseThrow(() -> new PaymentException(PaymentException.Reason.ILLEGAL_STATE,
                        "Online payment has no checkout session to refund"));
        final GatewayRefund refund = requestRefund(gatewayReference, payment.id(), "refund-" + payment.id().value());
        log.info("Stripe refund for payment {} is {}", payment.id().value(), refund.outcome());
        return refund;
    }

    @Override
    public void refundLateCharge(final String gatewayReference, final PaymentId paymentId) {
        requestRefund(gatewayReference, paymentId, "refund-late-" + gatewayReference);
        log.info("Stripe late charge {} of payment {} refunded", gatewayReference, paymentId.value());
    }

    private GatewayRefund requestRefund(final String gatewayReference, final PaymentId paymentId,
                                        final String idempotencyKey) {
        try {
            final Session session = this.stripeClient.checkout().sessions().retrieve(gatewayReference);
            try {
                final Refund refund = this.stripeClient.refunds().create(RefundCreateParams.builder()
                                .setPaymentIntent(session.getPaymentIntent())
                                .putMetadata("paymentId", String.valueOf(paymentId.value()))
                                .build(),
                        RequestOptions.builder().setIdempotencyKey(idempotencyKey).build());
                return new GatewayRefund("succeeded".equals(refund.getStatus())
                        ? RefundOutcome.COMPLETED : RefundOutcome.PENDING, session.getPaymentIntent());
            } catch (final InvalidRequestException exception) {
                if (!"charge_already_refunded".equals(exception.getCode())) {
                    throw exception;
                }
                log.warn("Stripe charge of payment {} is already refunded (request {})", paymentId.value(),
                        exception.getRequestId());
                return new GatewayRefund(RefundOutcome.COMPLETED, session.getPaymentIntent());
            }
        } catch (final InvalidRequestException exception) {
            log.warn("Stripe rejected refund for payment {} with code {} (request {})", paymentId.value(),
                    exception.getCode(), exception.getRequestId());
            throw new PaymentException(PaymentException.Reason.ILLEGAL_STATE, "Payment gateway rejected the refund");
        } catch (final StripeException exception) {
            log.warn("Stripe refund for payment {} failed with code {} (request {})", paymentId.value(),
                    exception.getCode(), exception.getRequestId());
            throw new PaymentException(PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable");
        }
    }
}
