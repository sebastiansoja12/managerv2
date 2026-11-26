package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.event.PaymentRefunded;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

@Slf4j
public class PaymentRefundService {

    private final PaymentRepository paymentRepository;

    private final PaymentGatewayServicePort paymentGatewayServicePort;

    private final DomainEventPublisher domainEventPublisher;

    private final CheckoutCloser checkoutCloser;

    private final Clock clock;

    public PaymentRefundService(final PaymentRepository paymentRepository,
                                final PaymentGatewayServicePort paymentGatewayServicePort,
                                final DomainEventPublisher domainEventPublisher,
                                final CheckoutCloser checkoutCloser,
                                final Clock clock) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayServicePort = paymentGatewayServicePort;
        this.domainEventPublisher = domainEventPublisher;
        this.checkoutCloser = checkoutCloser;
        this.clock = clock;
    }

    public void cancelForShipment(final PriceQuoteId priceQuoteId, final ShipmentStatus previousStatus) {
        this.paymentRepository.findByPriceQuote(priceQuoteId).ifPresentOrElse(payment -> {
            final PaymentStatus previousPaymentStatus = payment.status();
            payment.cancelForShipment(previousStatus, this.clock.instant(),
                    () -> this.paymentGatewayServicePort.refund(payment), () -> this.checkoutCloser.close(payment));
            record(payment, previousPaymentStatus);
            if (previousStatus == null && PaymentStatus.PAID.equals(payment.status())) {
                log.warn("Paid payment {} was not refunded, canceled shipment has no previous status",
                        payment.id().value());
            }
        }, () -> log.warn("Canceled shipment has no payment for price quote {}", priceQuoteId.value()));
    }

    public Payment requestRefund(final PaymentId paymentId) {
        final Payment payment = find(paymentId);
        final PaymentStatus previousStatus = payment.status();
        payment.requestRefund(this.clock.instant(), () -> this.paymentGatewayServicePort.refund(payment));
        record(payment, previousStatus);
        return payment;
    }

    public Payment confirmCounterRefund(final PaymentId paymentId, final UserId refundedBy) {
        final Payment payment = find(paymentId);
        final PaymentStatus previousStatus = payment.status();
        payment.markRefundedAtCounter(refundedBy, this.clock.instant());
        record(payment, previousStatus);
        return payment;
    }

    public void confirmGatewayRefund(final Payment payment) {
        final PaymentStatus previousStatus = payment.status();
        payment.markRefunded(this.clock.instant());
        record(payment, previousStatus);
    }

    private Payment find(final PaymentId paymentId) {
        return this.paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(PaymentException.Reason.NOT_FOUND, "Payment was not found"));
    }

    private void record(final Payment payment, final PaymentStatus previousStatus) {
        if (previousStatus == payment.status()) {
            return;
        }
        this.paymentRepository.save(payment);
        if (PaymentStatus.REFUNDED.equals(payment.status())) {
            this.domainEventPublisher.publish(new PaymentRefunded(payment.snapshot(), this.clock.instant()));
        }
    }
}
