package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.primary.command.ConfirmGatewayRefundCommand;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.application.port.secondary.ProcessedWebhookRepository;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.billing.payment.domain.model.Payment;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.util.Optional;

@Slf4j
public class OnlinePaymentConfirmationService {

    private final OnlinePaymentFinder onlinePaymentFinder;

    private final ProcessedWebhookRepository processedWebhookRepository;

    private final OnlinePaymentCompletion onlinePaymentCompletion;

    private final PaymentRefundService paymentRefundService;

    private final Clock clock;

    public OnlinePaymentConfirmationService(final OnlinePaymentFinder onlinePaymentFinder,
                                            final ProcessedWebhookRepository processedWebhookRepository,
                                            final OnlinePaymentCompletion onlinePaymentCompletion,
                                            final PaymentRefundService paymentRefundService,
                                            final Clock clock) {
        this.onlinePaymentFinder = onlinePaymentFinder;
        this.processedWebhookRepository = processedWebhookRepository;
        this.onlinePaymentCompletion = onlinePaymentCompletion;
        this.paymentRefundService = paymentRefundService;
        this.clock = clock;
    }

    public Payment confirm(final ConfirmOnlinePaymentCommand command) {
        final boolean alreadyProcessed = this.processedWebhookRepository.isProcessed(command.webhookEventId());
        final Payment payment = this.onlinePaymentFinder.find(command)
                .orElseThrow(() -> new PaymentException(PaymentException.Reason.NOT_FOUND,
                        "Payment of the checkout session was not found"));
        if (!alreadyProcessed) {
            this.onlinePaymentCompletion.complete(payment, command.gatewayReference(),
                    command.gatewayPaymentIntent());
            this.processedWebhookRepository.markProcessed(command.webhookEventId(), this.clock.instant());
        }
        return payment;
    }

    public void confirmRefund(final ConfirmGatewayRefundCommand command) {
        if (this.processedWebhookRepository.isProcessed(command.webhookEventId())) {
            return;
        }
        final Optional<Payment> payment = this.onlinePaymentFinder.find(command);
        if (payment.isEmpty() && this.onlinePaymentFinder.isLateChargeRefund(command)) {
            log.info("Refund of late charge {} confirmed", command.gatewayPaymentIntent());
            this.processedWebhookRepository.markProcessed(command.webhookEventId(), this.clock.instant());
            return;
        }
        this.paymentRefundService.confirmGatewayRefund(payment.orElseThrow(() ->
                new PaymentNotReadyException("Payment of the refunded charge is not known yet")));
        this.processedWebhookRepository.markProcessed(command.webhookEventId(), this.clock.instant());
    }
}
