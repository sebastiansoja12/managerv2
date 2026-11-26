package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.secondary.LateChargeRefundRepository;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.enumeration.RefundOutcome;
import com.warehouse.billing.payment.domain.model.LateChargeRefund;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.model.PaymentFixture;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.domain.vo.GatewayRefund;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleStateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;

import static com.warehouse.billing.payment.domain.model.PaymentFixture.DEPARTMENT_ID;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.NOW;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.PRICE_QUOTE_ID;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = PaymentPersistenceTestConfiguration.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class PaymentPersistenceIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LateChargeRefundRepository lateChargeRefundRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void shouldStoreRefundAndResolvePaymentByPaymentIntent() {
        final TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);
        final Payment payment = PaymentFixture.newPending(new PaymentId(6L), new PriceQuoteId(26L));
        payment.attachCheckout(new CheckoutSession("cs_refund", "https://checkout/cs_refund", NOW.plusSeconds(3600)),
                NOW);
        payment.markPaidOnline(NOW, "cs_refund", "pi_refund", () -> {
        });
        payment.requestRefund(NOW, () -> new GatewayRefund(RefundOutcome.PENDING, "pi_refund"));
        transaction.executeWithoutResult(status -> this.paymentRepository.save(payment));

        final Payment stored = transaction.execute(status ->
                this.paymentRepository.findByGatewayPaymentIntent("pi_refund").orElseThrow());

        assertThat(stored.status()).isEqualTo(PaymentStatus.REFUND_PENDING);
        assertThat(stored.refundRequestedAt()).isEqualTo(NOW);
        assertThat(stored.gatewayPaymentIntent()).isEqualTo("pi_refund");
        assertThat(this.paymentRepository.findOperatorByGatewayPaymentIntent("pi_refund"))
                .contains(PaymentPersistenceTestConfiguration.OPERATOR_ID);
        assertThat(this.paymentRepository.findOperatorByGatewayPaymentIntent("pi_unknown")).isEmpty();
    }

    @Test
    void shouldStoreLateChargeRefund() {
        final TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);
        final Payment payment = PaymentFixture.newPending(new PaymentId(7L), new PriceQuoteId(27L));

        transaction.executeWithoutResult(status -> this.lateChargeRefundRepository.save(
                new LateChargeRefund("cs_late_persisted", "pi_late_persisted", payment.id(), payment.amount(), NOW)));
        final Boolean stored = transaction.execute(status ->
                this.lateChargeRefundRepository.exists("cs_late_persisted"));
        final Boolean other = transaction.execute(status -> this.lateChargeRefundRepository.exists("cs_other"));
        final Boolean byIntent = transaction.execute(status ->
                this.lateChargeRefundRepository.existsByGatewayPaymentIntent("pi_late_persisted"));

        assertThat(stored).isTrue();
        assertThat(other).isFalse();
        assertThat(byIntent).isTrue();
        assertThat(this.lateChargeRefundRepository.findOperatorByGatewayPaymentIntent("pi_late_persisted"))
                .contains(PaymentPersistenceTestConfiguration.OPERATOR_ID);
        assertThat(this.lateChargeRefundRepository.findOperatorByGatewayPaymentIntent("pi_unknown")).isEmpty();
    }

    @Test
    void shouldRejectUpdateOfStalePaymentVersion() {
        final TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);
        transaction.executeWithoutResult(status -> this.paymentRepository.save(PaymentFixture.newPending()));
        final Payment first = transaction.execute(status ->
                this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow());
        final Payment stale = transaction.execute(status ->
                this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow());
        first.markPaid(PaymentMethod.CASH, USER_ID, DEPARTMENT_ID, NOW);
        transaction.executeWithoutResult(status -> this.paymentRepository.save(first));
        stale.markPaid(PaymentMethod.CARD, USER_ID, DEPARTMENT_ID, NOW);

        assertThatThrownBy(() -> transaction.executeWithoutResult(status -> this.paymentRepository.save(stale)))
                .isInstanceOfAny(OptimisticLockException.class, OptimisticLockingFailureException.class,
                        StaleStateException.class);
        final Payment stored = transaction.execute(status ->
                this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow());
        assertThat(stored.status()).isEqualTo(PaymentStatus.PAID);
        assertThat(stored.method()).isEqualTo(PaymentMethod.CASH);
        assertThat(stored.version()).isEqualTo(1L);
        assertThat(stored.amount().getAmount()).isEqualByComparingTo("17.22");
        assertThat(stored.department()).isEqualTo(DEPARTMENT_ID);
    }

    @Test
    void shouldStoreCheckoutSessionAndResolveItsOperator() {
        final TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);
        final Payment payment = PaymentFixture.newPending(new PaymentId(2L), new PriceQuoteId(21L));
        final CheckoutSession session = new CheckoutSession("cs_test_persisted", "https://checkout/cs_test_persisted",
                NOW.plusSeconds(3600));
        payment.attachCheckout(session, NOW);
        transaction.executeWithoutResult(status -> this.paymentRepository.save(payment));

        final Payment stored = transaction.execute(status ->
                this.paymentRepository.findByGatewayReference("cs_test_persisted").orElseThrow());

        assertThat(stored.checkoutSession()).contains(session);
        assertThat(stored.checkoutAttempt()).isEqualTo(1);
        assertThat(this.paymentRepository.findOperatorByGatewayReference("cs_test_persisted"))
                .contains(PaymentPersistenceTestConfiguration.OPERATOR_ID);
        assertThat(this.paymentRepository.findOperatorByGatewayReference("cs_unknown")).isEmpty();
        assertThat(this.paymentRepository.findOperatorByPaymentId(new PaymentId(2L)))
                .contains(PaymentPersistenceTestConfiguration.OPERATOR_ID);
    }

    @Test
    void shouldFindOldestOverduePendingPayments() {
        final TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);
        final Instant now = Instant.parse("2026-11-26T00:00:00Z");
        transaction.executeWithoutResult(status -> {
            this.paymentRepository.save(PaymentFixture.pendingDue(new PaymentId(3L), new PriceQuoteId(23L),
                    Instant.parse("2026-11-25T10:00:00Z")));
            this.paymentRepository.save(PaymentFixture.pendingDue(new PaymentId(4L), new PriceQuoteId(24L),
                    Instant.parse("2026-11-25T09:00:00Z")));
            this.paymentRepository.save(PaymentFixture.pendingDue(new PaymentId(5L), new PriceQuoteId(25L),
                    Instant.parse("2026-11-26T12:00:00Z")));
        });

        final List<Payment> all = transaction.execute(status -> this.paymentRepository.findOverduePending(now, 10));
        final List<Payment> oldest = transaction.execute(status ->
                this.paymentRepository.findOverduePending(now, 1));

        assertThat(all).extracting(Payment::id).containsExactly(new PaymentId(4L), new PaymentId(3L));
        assertThat(oldest).extracting(Payment::id).containsExactly(new PaymentId(4L));
    }
}
