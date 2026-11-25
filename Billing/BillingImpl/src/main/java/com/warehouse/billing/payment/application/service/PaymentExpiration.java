package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.event.PaymentExpired;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;

import java.time.Instant;

public class PaymentExpiration {

    private final PaymentRepository paymentRepository;

    private final DomainEventPublisher domainEventPublisher;

    public PaymentExpiration(final PaymentRepository paymentRepository,
                             final DomainEventPublisher domainEventPublisher) {
        this.paymentRepository = paymentRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public void expire(final Payment payment, final Instant now) {
        final PaymentStatus previousStatus = payment.status();
        payment.expire(now);
        if (previousStatus == payment.status()) {
            return;
        }
        this.paymentRepository.save(payment);
        this.domainEventPublisher.publish(new PaymentExpired(payment.snapshot(), now));
    }
}
