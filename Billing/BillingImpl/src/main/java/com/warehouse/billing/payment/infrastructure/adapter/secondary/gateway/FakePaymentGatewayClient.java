package com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway;

import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.domain.enumeration.CheckoutClosure;
import com.warehouse.billing.payment.domain.enumeration.RefundOutcome;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.service.CheckoutExpiryPolicy;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.domain.vo.GatewayRefund;
import com.warehouse.commonassets.identificator.PaymentId;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;

@Slf4j
public class FakePaymentGatewayClient implements PaymentGatewayServicePort {

    private final String checkoutBaseUrl;

    private final CheckoutExpiryPolicy checkoutExpiryPolicy;

    private final Clock clock;

    public FakePaymentGatewayClient(final String checkoutBaseUrl, final CheckoutExpiryPolicy checkoutExpiryPolicy,
                                    final Clock clock) {
        this.checkoutBaseUrl = checkoutBaseUrl;
        this.checkoutExpiryPolicy = checkoutExpiryPolicy;
        this.clock = clock;
    }

    @Override
    public CheckoutSession openCheckout(final Payment payment) {
        final Instant now = this.clock.instant();
        final Long paymentId = payment.id().value();
        log.info("Fake checkout opened for payment {}", paymentId);
        return new CheckoutSession("fake_cs_" + paymentId + "_" + now.toEpochMilli(),
                this.checkoutBaseUrl + "/" + paymentId + "/COMPLETED",
                this.checkoutExpiryPolicy.expiresAt(payment.deadline(), now));
    }

    @Override
    public CheckoutClosure expireCheckout(final String gatewayReference) {
        log.info("Fake checkout {} expired", gatewayReference);
        return CheckoutClosure.EXPIRED;
    }

    @Override
    public GatewayRefund refund(final Payment payment) {
        log.info("Fake refund completed for payment {}", payment.id().value());
        return new GatewayRefund(RefundOutcome.COMPLETED, "fake_pi_" + payment.id().value());
    }

    @Override
    public void refundLateCharge(final String gatewayReference, final PaymentId paymentId) {
        log.info("Fake late charge {} of payment {} refunded", gatewayReference, paymentId.value());
    }
}
