package com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway;

import com.stripe.param.checkout.SessionCreateParams;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.service.CheckoutExpiryPolicy;
import com.warehouse.commonassets.enumeration.Currency;

import java.math.RoundingMode;
import java.time.Instant;
import java.util.Locale;

public class StripeCheckoutSessionParamsFactory {

    private final String successUrl;

    private final String cancelUrl;

    private final CheckoutExpiryPolicy checkoutExpiryPolicy;

    public StripeCheckoutSessionParamsFactory(final String successUrl, final String cancelUrl,
                                              final CheckoutExpiryPolicy checkoutExpiryPolicy) {
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
        this.checkoutExpiryPolicy = checkoutExpiryPolicy;
    }

    public String idempotencyKey(final Payment payment, final Instant now) {
        return "checkout-" + payment.id().value() + "-" + payment.nextCheckoutAttempt() + "-"
                + this.checkoutExpiryPolicy.expiresAt(payment.deadline(), now).getEpochSecond();
    }

    public SessionCreateParams create(final Payment payment, final Instant now) {
        final Currency currency = payment.amount().getCurrency();
        final String stripeCurrency = currency.name().toLowerCase(Locale.ROOT);
        final String paymentId = String.valueOf(payment.id().value());
        final SessionCreateParams.Builder builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCurrency(stripeCurrency)
                .setSuccessUrl(this.successUrl)
                .setCancelUrl(this.cancelUrl)
                .setClientReferenceId(paymentId)
                .putMetadata("paymentId", paymentId)
                .setExpiresAt(this.checkoutExpiryPolicy.expiresAt(payment.deadline(), now).getEpochSecond())
                .addAllowedPaymentMethodType(SessionCreateParams.AllowedPaymentMethodType.CARD)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(stripeCurrency)
                                .setUnitAmount(payment.amount().getAmount().setScale(2, RoundingMode.HALF_UP)
                                        .movePointRight(2).longValueExact())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Shipment")
                                        .build())
                                .build())
                        .build());
        if (Currency.PLN.equals(currency)) {
            builder.addAllowedPaymentMethodType(SessionCreateParams.AllowedPaymentMethodType.BLIK);
        }
        return builder.build();
    }
}
