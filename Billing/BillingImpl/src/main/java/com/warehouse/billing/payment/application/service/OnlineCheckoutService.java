package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.primary.result.CheckoutView;
import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.enumeration.CheckoutClosure;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.commonassets.identificator.PriceQuoteId;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

public class OnlineCheckoutService {

    private final PaymentRepository paymentRepository;

    private final PaymentOpeningService paymentOpeningService;

    private final PaymentGatewayServicePort paymentGatewayServicePort;

    private final OnlinePaymentCompletion onlinePaymentCompletion;

    private final Clock clock;

    public OnlineCheckoutService(final PaymentRepository paymentRepository,
                                 final PaymentOpeningService paymentOpeningService,
                                 final PaymentGatewayServicePort paymentGatewayServicePort,
                                 final OnlinePaymentCompletion onlinePaymentCompletion,
                                 final Clock clock) {
        this.paymentRepository = paymentRepository;
        this.paymentOpeningService = paymentOpeningService;
        this.paymentGatewayServicePort = paymentGatewayServicePort;
        this.onlinePaymentCompletion = onlinePaymentCompletion;
        this.clock = clock;
    }

    public CheckoutView open(final PriceQuoteId priceQuoteId) {
        final Instant now = this.clock.instant();
        final Payment payment = this.paymentRepository.findByPriceQuote(priceQuoteId)
                .or(() -> this.paymentOpeningService.open(priceQuoteId))
                .orElseThrow(() -> new PaymentException(PaymentException.Reason.NOT_FOUND,
                        "Price quote was not found"));
        if (!payment.acceptsOnlineCheckout(now)) {
            return new CheckoutView(payment.status(), payment.amount(), null, null);
        }
        final Optional<CheckoutSession> openCheckout = payment.openCheckout(now);
        if (openCheckout.isPresent()) {
            return new CheckoutView(payment.status(), payment.amount(), openCheckout.get().url(),
                    openCheckout.get().expiresAt());
        }
        final Optional<CheckoutSession> completedCheckout = payment.checkoutSession()
                .filter(previous -> CheckoutClosure.COMPLETED.equals(
                        this.paymentGatewayServicePort.expireCheckout(previous.gatewayReference())));
        if (completedCheckout.isPresent()) {
            this.onlinePaymentCompletion.complete(payment, completedCheckout.get().gatewayReference(), null);
            return new CheckoutView(payment.status(), payment.amount(), null, null);
        }
        final CheckoutSession session = this.paymentGatewayServicePort.openCheckout(payment);
        payment.attachCheckout(session, now);
        this.paymentRepository.save(payment);
        return new CheckoutView(payment.status(), payment.amount(), session.url(), session.expiresAt());
    }
}
