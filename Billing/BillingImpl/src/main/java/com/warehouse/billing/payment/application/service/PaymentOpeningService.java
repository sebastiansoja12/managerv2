package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.secondary.PaymentDeadlineServicePort;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.service.PaymentFactory;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PaymentOpeningService {

    private final PricingPort pricingPort;

    private final PaymentDeadlineServicePort paymentDeadlineServicePort;

    private final PaymentFactory paymentFactory;

    public PaymentOpeningService(final PricingPort pricingPort,
                                 final PaymentDeadlineServicePort paymentDeadlineServicePort,
                                 final PaymentFactory paymentFactory) {
        this.pricingPort = pricingPort;
        this.paymentDeadlineServicePort = paymentDeadlineServicePort;
        this.paymentFactory = paymentFactory;
    }

    public Optional<Payment> open(final PriceQuoteId priceQuoteId) {
        final Optional<Payment> payment = this.pricingPort.findQuote(priceQuoteId)
                .map(quote -> this.paymentFactory.create(quote.id(), quote.gross(), quote.quotedAt(),
                        this.paymentDeadlineServicePort.paymentDeadline()));
        if (payment.isEmpty()) {
            log.warn("Payment was not opened, price quote {} was not found", priceQuoteId.value());
        }
        return payment;
    }

    public Payment openForCounter(final PriceQuoteId priceQuoteId) {
        return open(priceQuoteId).orElseThrow(() -> new PaymentException(PaymentException.Reason.NOT_FOUND,
                "Price quote was not found"));
    }
}
