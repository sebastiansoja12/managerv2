package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.stripe.model.Event;
import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.StripeWebhookMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing/webhooks")
@ConditionalOnProperty(name = "billing.payment-gateway", havingValue = "stripe")
public class StripeWebhookController {

    private final PaymentPort paymentPort;

    private final StripeWebhookEventVerifier stripeWebhookEventVerifier;

    private final StripeWebhookMapper stripeWebhookMapper;

    public StripeWebhookController(final PaymentPort paymentPort,
                                   final StripeWebhookEventVerifier stripeWebhookEventVerifier,
                                   final StripeWebhookMapper stripeWebhookMapper) {
        this.paymentPort = paymentPort;
        this.stripeWebhookEventVerifier = stripeWebhookEventVerifier;
        this.stripeWebhookMapper = stripeWebhookMapper;
    }

    @PostMapping("/stripe")
    public ResponseEntity<Void> handle(@RequestBody final String payload,
                                       @RequestHeader(name = "Stripe-Signature", required = false)
                                       final String signature) {
        final Event event = this.stripeWebhookEventVerifier.verify(payload, signature);
        this.stripeWebhookMapper.toConfirmation(event).ifPresent(this.paymentPort::confirmOnlinePayment);
        this.stripeWebhookMapper.toRefundConfirmation(event).ifPresent(this.paymentPort::confirmGatewayRefund);
        return ResponseEntity.ok().build();
    }
}
