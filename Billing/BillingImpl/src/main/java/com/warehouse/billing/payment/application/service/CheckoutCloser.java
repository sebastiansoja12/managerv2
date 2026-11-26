package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutCloser {

    private final PaymentGatewayServicePort paymentGatewayServicePort;

    public CheckoutCloser(final PaymentGatewayServicePort paymentGatewayServicePort) {
        this.paymentGatewayServicePort = paymentGatewayServicePort;
    }

    public void close(final Payment payment) {
        payment.checkoutSession().map(CheckoutSession::gatewayReference).ifPresent(gatewayReference -> {
            try {
                this.paymentGatewayServicePort.expireCheckout(gatewayReference);
            } catch (final RuntimeException exception) {
                log.warn("Checkout of canceled payment {} was not expired: {}", payment.id().value(),
                        exception.getMessage());
            }
        });
    }
}
