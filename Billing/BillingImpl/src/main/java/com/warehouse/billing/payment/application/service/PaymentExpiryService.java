package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.enumeration.CheckoutClosure;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Optional;

@Slf4j
public class PaymentExpiryService {

    private static final int BATCH_SIZE = 200;

    private final PaymentRepository paymentRepository;

    private final PaymentGatewayServicePort paymentGatewayServicePort;

    private final OnlinePaymentCompletion onlinePaymentCompletion;

    private final PaymentExpiration paymentExpiration;

    private final TransactionTemplate transactionTemplate;

    public PaymentExpiryService(final PaymentRepository paymentRepository,
                                final PaymentGatewayServicePort paymentGatewayServicePort,
                                final OnlinePaymentCompletion onlinePaymentCompletion,
                                final PaymentExpiration paymentExpiration,
                                final TransactionTemplate transactionTemplate) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayServicePort = paymentGatewayServicePort;
        this.onlinePaymentCompletion = onlinePaymentCompletion;
        this.paymentExpiration = paymentExpiration;
        this.transactionTemplate = transactionTemplate;
    }

    public void expireOverdue(final Instant now) {
        for (final Payment payment : this.paymentRepository.findOverduePending(now, BATCH_SIZE)) {
            try {
                final Optional<String> completedCheckout = payment.checkoutSession()
                        .map(CheckoutSession::gatewayReference)
                        .filter(gatewayReference -> CheckoutClosure.COMPLETED.equals(
                                this.paymentGatewayServicePort.expireCheckout(gatewayReference)));
                this.transactionTemplate.executeWithoutResult(status -> completedCheckout.ifPresentOrElse(
                        gatewayReference -> this.onlinePaymentCompletion.complete(payment, gatewayReference, null),
                        () -> this.paymentExpiration.expire(payment, now)));
            } catch (final RuntimeException exception) {
                log.warn("Overdue payment {} was not expired: {}", payment.id().value(), exception.getMessage());
            }
        }
    }
}
