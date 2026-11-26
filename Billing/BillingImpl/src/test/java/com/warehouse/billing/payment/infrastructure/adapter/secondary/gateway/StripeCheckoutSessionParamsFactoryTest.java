package com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway;

import com.stripe.param.checkout.SessionCreateParams;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.service.CheckoutExpiryPolicy;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class StripeCheckoutSessionParamsFactoryTest {

    private static final Instant NOW = Instant.parse("2026-11-24T12:00:00Z");

    private final StripeCheckoutSessionParamsFactory factory = new StripeCheckoutSessionParamsFactory(
            "http://localhost:3000/payment/success", "http://localhost:3000/payment/cancel",
            new CheckoutExpiryPolicy());

    @Test
    void shouldOfferBlikOnlyForPln() {
        final SessionCreateParams pln = this.factory.create(payment(Currency.PLN, NOW.plus(Duration.ofHours(72))), NOW);
        final SessionCreateParams eur = this.factory.create(payment(Currency.EUR, NOW.plus(Duration.ofHours(72))), NOW);

        assertThat(pln.getAllowedPaymentMethodTypes()).containsExactly(
                SessionCreateParams.AllowedPaymentMethodType.CARD, SessionCreateParams.AllowedPaymentMethodType.BLIK);
        assertThat(eur.getAllowedPaymentMethodTypes()).containsExactly(
                SessionCreateParams.AllowedPaymentMethodType.CARD);
    }

    @Test
    void shouldBuildPaymentModeSessionFromPayment() {
        final SessionCreateParams params = this.factory.create(payment(Currency.PLN, NOW.plus(Duration.ofHours(72))),
                NOW);

        assertThat(params.getMode()).isEqualTo(SessionCreateParams.Mode.PAYMENT);
        assertThat(params.getCurrency()).isEqualTo("pln");
        assertThat(params.getMetadata()).containsEntry("paymentId", "42");
        assertThat(params.getSuccessUrl()).isEqualTo("http://localhost:3000/payment/success");
        assertThat(params.getCancelUrl()).isEqualTo("http://localhost:3000/payment/cancel");
        assertThat(params.getLineItems()).hasSize(1);
        final SessionCreateParams.LineItem line = params.getLineItems().getFirst();
        assertThat(line.getQuantity()).isEqualTo(1L);
        assertThat(line.getPriceData().getUnitAmount()).isEqualTo(1722L);
        assertThat(line.getPriceData().getCurrency()).isEqualTo("pln");
    }

    @Test
    void shouldExpireSessionAtDeadlineWhenItIsBeforeOneDay() {
        final Instant deadline = NOW.plus(Duration.ofHours(5));

        final SessionCreateParams params = this.factory.create(payment(Currency.PLN, deadline), NOW);

        assertThat(params.getExpiresAt()).isEqualTo(deadline.getEpochSecond());
    }

    @Test
    void shouldExpireSessionAfterOneDayWhenDeadlineIsLater() {
        final SessionCreateParams params = this.factory.create(payment(Currency.PLN, NOW.plus(Duration.ofHours(72))),
                NOW);

        assertThat(params.getExpiresAt()).isEqualTo(NOW.plus(Duration.ofHours(24)).getEpochSecond());
    }

    @Test
    void shouldUseNewIdempotencyKeyAfterCheckoutExpired() {
        final Payment payment = payment(Currency.PLN, NOW.plus(Duration.ofHours(72)));
        final String firstKey = this.factory.idempotencyKey(payment, NOW);
        payment.attachCheckout(new CheckoutSession("cs_1", "https://checkout/cs_1", NOW.plusSeconds(1800)), NOW);

        final String keyAfterExpiry = this.factory.idempotencyKey(payment, NOW);

        final long expiresAt = NOW.plus(Duration.ofHours(24)).getEpochSecond();
        assertThat(firstKey).isEqualTo("checkout-42-1-" + expiresAt);
        assertThat(keyAfterExpiry).isEqualTo("checkout-42-2-" + expiresAt);
    }

    @Test
    void shouldReuseIdempotencyKeyOnlyForIdenticalSessionParameters() {
        final Payment payment = payment(Currency.PLN, NOW.plus(Duration.ofHours(72)));

        final String key = this.factory.idempotencyKey(payment, NOW);
        final String sameSecondRetry = this.factory.idempotencyKey(payment, NOW);
        final String laterRetry = this.factory.idempotencyKey(payment, NOW.plusSeconds(5));

        assertThat(sameSecondRetry).isEqualTo(key);
        assertThat(laterRetry).isNotEqualTo(key);
        assertThat(this.factory.create(payment, NOW.plusSeconds(5)).getExpiresAt())
                .isNotEqualTo(this.factory.create(payment, NOW).getExpiresAt());
    }

    private Payment payment(final Currency currency, final Instant deadline) {
        return new Payment(new PaymentId(42L), new PriceQuoteId(20L), Money.from(new BigDecimal("17.22"), currency),
                null, null, PaymentStatus.PENDING, null, 0, deadline, NOW, null, null, null, null, null, null,
                null, null, 0L);
    }
}
