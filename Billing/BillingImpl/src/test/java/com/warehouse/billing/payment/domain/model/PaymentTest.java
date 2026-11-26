package com.warehouse.billing.payment.domain.model;

import com.warehouse.billing.payment.domain.enumeration.PaymentChannel;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.commonassets.identificator.TaxId;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.warehouse.billing.payment.domain.model.PaymentFixture.DEPARTMENT_ID;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.NOW;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    private static final Runnable NO_LATE_CHARGE = () -> {
    };

    @Test
    void shouldMarkPendingPaymentAsPaidAtCounter() {
        final Payment payment = PaymentFixture.pending();

        payment.markPaidAtCounter(PaymentMethod.CARD, USER_ID, DEPARTMENT_ID, TaxId.of("5260250995"), NOW);

        assertThat(payment.status()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment.method()).isEqualTo(PaymentMethod.CARD);
        assertThat(payment.channel()).isEqualTo(PaymentChannel.COUNTER);
        assertThat(payment.recordedBy()).isEqualTo(USER_ID);
        assertThat(payment.department()).isEqualTo(DEPARTMENT_ID);
        assertThat(payment.buyerTaxId()).isEqualTo(TaxId.of("5260250995"));
        assertThat(payment.paidAt()).isEqualTo(NOW);
    }

    @Test
    void shouldIgnoreRepeatedPaymentWithSameMethod() {
        final Payment payment = PaymentFixture.pending();
        payment.markPaid(PaymentMethod.CASH, USER_ID, DEPARTMENT_ID, NOW);

        payment.markPaidAtCounter(PaymentMethod.CASH, USER_ID, DEPARTMENT_ID, TaxId.of("5260250995"),
                NOW.plusSeconds(60));

        assertThat(payment.paidAt()).isEqualTo(NOW);
        assertThat(payment.buyerTaxId()).isNull();
    }

    @Test
    void shouldRejectRepeatedPaymentWithDifferentMethod() {
        final Payment payment = PaymentFixture.pending();
        payment.markPaid(PaymentMethod.CASH, USER_ID, DEPARTMENT_ID, NOW);

        assertThatThrownBy(() -> payment.markPaid(PaymentMethod.CARD, USER_ID, DEPARTMENT_ID, NOW))
                .isInstanceOf(PaymentException.class);
    }

    @Test
    void shouldRejectPaymentOfExpiredPayment() {
        final Payment payment = PaymentFixture.withStatus(PaymentStatus.EXPIRED);

        assertThatThrownBy(() -> payment.markPaid(PaymentMethod.CASH, USER_ID, DEPARTMENT_ID, NOW))
                .isInstanceOf(PaymentException.class);
    }

    @Test
    void shouldRejectStripeMethodAtCounter() {
        final Payment payment = PaymentFixture.pending();

        assertThatThrownBy(() -> payment.markPaidAtCounter(PaymentMethod.STRIPE, USER_ID, DEPARTMENT_ID, null, NOW))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.INVALID_REQUEST));
        assertThat(payment.status()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void shouldAttachCheckoutAndSwitchToOnlineChannel() {
        final Payment payment = PaymentFixture.pending();
        final CheckoutSession session = new CheckoutSession("cs_1", "https://checkout/cs_1", NOW.plusSeconds(3600));

        payment.attachCheckout(session, NOW);

        assertThat(payment.checkoutSession()).contains(session);
        assertThat(payment.channel()).isEqualTo(PaymentChannel.ONLINE);
        assertThat(payment.openCheckout(NOW)).contains(session);
        assertThat(payment.openCheckout(NOW.plusSeconds(3600))).isEmpty();
    }

    @Test
    void shouldRejectCheckoutForPaidPayment() {
        final Payment payment = PaymentFixture.pending();
        payment.markPaid(PaymentMethod.CASH, USER_ID, DEPARTMENT_ID, NOW);

        assertThatThrownBy(() -> payment.attachCheckout(
                new CheckoutSession("cs_1", "https://checkout/cs_1", NOW.plusSeconds(3600)), NOW))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.ILLEGAL_STATE));
    }

    @Test
    void shouldRejectCheckoutAfterDeadline() {
        final Payment payment = PaymentFixture.pending();

        assertThatThrownBy(() -> payment.attachCheckout(
                new CheckoutSession("cs_1", "https://checkout/cs_1", NOW.plusSeconds(3600)), payment.deadline()))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.ILLEGAL_STATE));
        assertThat(payment.acceptsOnlineCheckout(payment.deadline())).isFalse();
        assertThat(payment.checkoutSession()).isEmpty();
    }

    @Test
    void shouldMarkPaidOnlineOnlyOnce() {
        final Payment payment = PaymentFixture.pending();
        payment.markPaidOnline(NOW, null, "pi_1", NO_LATE_CHARGE);

        payment.markPaidOnline(NOW.plusSeconds(60), "cs_other", "pi_other", NO_LATE_CHARGE);

        assertThat(payment.status()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment.method()).isEqualTo(PaymentMethod.STRIPE);
        assertThat(payment.channel()).isEqualTo(PaymentChannel.ONLINE);
        assertThat(payment.paidAt()).isEqualTo(NOW);
    }

    @Test
    void shouldRecordPayingSessionWhenItDiffersFromStoredSession() {
        final Payment payment = PaymentFixture.pending();
        payment.attachCheckout(new CheckoutSession("cs_new", "https://checkout/cs_new", NOW.plusSeconds(3600)), NOW);

        payment.markPaidOnline(NOW, "cs_old", null, NO_LATE_CHARGE);

        assertThat(payment.checkoutSession()).map(CheckoutSession::gatewayReference).contains("cs_old");
        assertThat(payment.status()).isEqualTo(PaymentStatus.PAID);
    }

    @Test
    void shouldKeepStoredSessionWhenPaidWithoutReference() {
        final Payment payment = PaymentFixture.pending();
        final CheckoutSession session = new CheckoutSession("cs_new", "https://checkout/cs_new", NOW.plusSeconds(3600));
        payment.attachCheckout(session, NOW);

        payment.markPaidOnline(NOW, null, null, NO_LATE_CHARGE);

        assertThat(payment.checkoutSession()).contains(session);
    }

    @Test
    void shouldRefundLateOnlinePaymentOfExpiredPayment() {
        final Payment payment = PaymentFixture.withStatus(PaymentStatus.EXPIRED);
        final AtomicInteger lateChargeRefunds = new AtomicInteger();

        payment.markPaidOnline(NOW, "cs_1", "pi_1", lateChargeRefunds::incrementAndGet);

        assertThat(lateChargeRefunds).hasValue(1);
        assertThat(payment.status()).isEqualTo(PaymentStatus.EXPIRED);
        assertThat(payment.gatewayPaymentIntent()).isNull();
    }

    @Test
    void shouldStorePaymentIntentWhenPaidOnline() {
        final Payment payment = PaymentFixture.pending();

        payment.markPaidOnline(NOW, "cs_1", "pi_1", NO_LATE_CHARGE);

        assertThat(payment.gatewayPaymentIntent()).isEqualTo("pi_1");
    }

    @Test
    void shouldExpirePendingPaymentPastDeadline() {
        final Payment payment = PaymentFixture.pending();

        payment.expire(payment.deadline().plusSeconds(1));

        assertThat(payment.status()).isEqualTo(PaymentStatus.EXPIRED);
    }

    @Test
    void shouldNotExpirePendingPaymentBeforeDeadline() {
        final Payment payment = PaymentFixture.pending();

        payment.expire(payment.deadline().minusSeconds(1));

        assertThat(payment.status()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void shouldNotExpirePaidPayment() {
        final Payment payment = PaymentFixture.withStatus(PaymentStatus.PAID);

        payment.expire(payment.deadline().plusSeconds(1));

        assertThat(payment.status()).isEqualTo(PaymentStatus.PAID);
    }
}
