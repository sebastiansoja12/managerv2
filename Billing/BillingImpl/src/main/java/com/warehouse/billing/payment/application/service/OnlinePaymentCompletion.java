package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.event.PaymentPaid;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;

import java.time.Clock;

public class OnlinePaymentCompletion {

    private final PaymentRepository paymentRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final LateChargeRefundService lateChargeRefundService;

    private final Clock clock;

    public OnlinePaymentCompletion(final PaymentRepository paymentRepository,
                                   final DomainEventPublisher domainEventPublisher,
                                   final LateChargeRefundService lateChargeRefundService,
                                   final Clock clock) {
        this.paymentRepository = paymentRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.lateChargeRefundService = lateChargeRefundService;
        this.clock = clock;
    }

    public void complete(final Payment payment, final String gatewayReference, final String gatewayPaymentIntent) {
        final PaymentStatus previousStatus = payment.status();
        payment.markPaidOnline(this.clock.instant(), gatewayReference, gatewayPaymentIntent,
                () -> this.lateChargeRefundService.refund(payment, gatewayReference, gatewayPaymentIntent));
        if (previousStatus == payment.status()) {
            return;
        }
        this.paymentRepository.save(payment);
        this.domainEventPublisher.publish(new PaymentPaid(payment.snapshot(), this.clock.instant()));
    }
}
