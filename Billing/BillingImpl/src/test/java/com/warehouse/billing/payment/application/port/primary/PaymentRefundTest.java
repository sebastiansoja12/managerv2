package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.primary.command.ConfirmGatewayRefundCommand;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.application.port.secondary.PaymentActorServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentOperatorContextServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentQuoteLookupServicePort;
import com.warehouse.billing.payment.application.service.CheckoutCloser;
import com.warehouse.billing.payment.application.service.LateChargeRefundService;
import com.warehouse.billing.payment.application.service.OnlineCheckoutService;
import com.warehouse.billing.payment.application.service.OnlinePaymentCompletion;
import com.warehouse.billing.payment.application.service.OnlinePaymentConfirmationService;
import com.warehouse.billing.payment.application.service.OnlinePaymentFinder;
import com.warehouse.billing.payment.application.service.PaymentExpiryService;
import com.warehouse.billing.payment.application.service.PaymentOpeningService;
import com.warehouse.billing.payment.application.service.PaymentRefundService;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.enumeration.RefundOutcome;
import com.warehouse.billing.payment.domain.event.PaymentPaid;
import com.warehouse.billing.payment.domain.event.PaymentRefunded;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.billing.payment.domain.model.LateChargeRefund;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.model.PaymentFixture;
import com.warehouse.billing.payment.domain.service.LateChargeRefundFactory;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.domain.vo.GatewayRefund;
import com.warehouse.billing.payment.domain.vo.PaymentActor;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.WebhookEventId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.function.Supplier;

import static com.warehouse.billing.payment.domain.model.PaymentFixture.DEPARTMENT_ID;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.NOW;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.PRICE_QUOTE_ID;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PaymentRefundTest {

    private InMemoryPaymentRepository paymentRepository;

    private InMemoryLateChargeRefundRepository lateChargeRefundRepository;

    private DomainEventPublisher domainEventPublisher;

    private PaymentGatewayServicePort paymentGatewayServicePort;

    private PaymentPortImpl paymentPort;

    @BeforeEach
    void setUp() {
        this.paymentRepository = new InMemoryPaymentRepository();
        this.lateChargeRefundRepository = new InMemoryLateChargeRefundRepository();
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        this.paymentGatewayServicePort = mock(PaymentGatewayServicePort.class);
        when(this.paymentGatewayServicePort.refund(any())).thenReturn(new GatewayRefund(RefundOutcome.PENDING, "pi_1"));
        final PaymentActorServicePort paymentActorServicePort = mock(PaymentActorServicePort.class);
        when(paymentActorServicePort.currentActor()).thenReturn(new PaymentActor(USER_ID, DEPARTMENT_ID));
        final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        final PaymentRefundService paymentRefundService = new PaymentRefundService(this.paymentRepository,
                this.paymentGatewayServicePort, this.domainEventPublisher,
                new CheckoutCloser(this.paymentGatewayServicePort), clock);
        final OnlinePaymentCompletion onlinePaymentCompletion = new OnlinePaymentCompletion(this.paymentRepository,
                this.domainEventPublisher, new LateChargeRefundService(this.lateChargeRefundRepository,
                this.paymentGatewayServicePort, new LateChargeRefundFactory(clock)), clock);
        final OnlinePaymentFinder onlinePaymentFinder = new OnlinePaymentFinder(this.paymentRepository,
                this.lateChargeRefundRepository);
        this.paymentPort = new PaymentPortImpl(this.paymentRepository, mock(PaymentOpeningService.class),
                paymentActorServicePort, this.domainEventPublisher, clock, mock(OnlineCheckoutService.class),
                new OnlinePaymentConfirmationService(onlinePaymentFinder, new InMemoryProcessedWebhookRepository(),
                        onlinePaymentCompletion, paymentRefundService, clock),
                mock(PaymentQuoteLookupServicePort.class), new DirectOperatorContext(), onlinePaymentFinder,
                mock(PaymentExpiryService.class), paymentRefundService);
    }

    @Test
    void shouldCancelPendingPaymentWhenShipmentCanceled() {
        this.paymentRepository.save(PaymentFixture.pending());

        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.CREATED);

        assertThat(payment().status()).isEqualTo(PaymentStatus.CANCELED);
        verifyNoInteractions(this.paymentGatewayServicePort);
    }

    @Test
    void shouldRefundOnlinePaymentCanceledBeforeCollection() {
        this.paymentRepository.save(paidOnline());

        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.PREPARED);

        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUND_PENDING);
        verify(this.paymentGatewayServicePort).refund(payment());
        verify(this.domainEventPublisher, never()).publish(isA(PaymentRefunded.class));

        this.paymentPort.confirmGatewayRefund(new ConfirmGatewayRefundCommand("pi_1", new WebhookEventId("evt_r1")));

        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(payment().refundedAt()).isEqualTo(NOW);
        final PaymentRefunded event = publishedRefund();
        assertThat(event.getSnapshot().priceQuoteId()).isEqualTo(PRICE_QUOTE_ID);
        assertThat(event.getSnapshot().amount().getAmount()).isEqualByComparingTo("17.22");
        assertThat(event.getSnapshot().refundedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldWaitForWorkerWhenCounterPaymentCanceledBeforeCollection() {
        this.paymentRepository.save(paidAtCounter());

        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.CREATED);

        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUND_PENDING);
        verifyNoInteractions(this.paymentGatewayServicePort);
        verify(this.domainEventPublisher, never()).publish(any());

        this.paymentPort.confirmCounterRefund(payment().id());

        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(payment().refundedBy()).isEqualTo(USER_ID);
        assertThat(publishedRefund().getSnapshot().paymentId()).isEqualTo(payment().id());
    }

    @Test
    void shouldNotRefundAutomaticallyAfterCollection() {
        this.paymentRepository.save(paidOnline());
        final int saves = this.paymentRepository.saves();

        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.COLLECTED);

        assertThat(payment().status()).isEqualTo(PaymentStatus.PAID);
        assertThat(this.paymentRepository.saves()).isEqualTo(saves);
        verifyNoInteractions(this.paymentGatewayServicePort);
    }

    @Test
    void shouldAllowManualRefundAfterCollection() {
        this.paymentRepository.save(paidOnline());
        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.COLLECTED);

        final Payment refunded = this.paymentPort.requestManualRefund(payment().id());

        assertThat(refunded.status()).isEqualTo(PaymentStatus.REFUND_PENDING);
        verify(this.paymentGatewayServicePort).refund(refunded);
    }

    @Test
    void shouldRefundLateCompletionOfExpiredPayment() {
        this.paymentRepository.save(PaymentFixture.withStatus(PaymentStatus.EXPIRED));

        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_late", "pi_late",
                new WebhookEventId("evt_late")));

        assertThat(payment().status()).isEqualTo(PaymentStatus.EXPIRED);
        verify(this.paymentGatewayServicePort).refundLateCharge("cs_late", payment().id());
        assertThat(this.lateChargeRefundRepository.all()).hasSize(1);
        final LateChargeRefund refund = this.lateChargeRefundRepository.all().get(0);
        assertThat(refund.paymentId()).isEqualTo(payment().id());
        assertThat(refund.gatewayReference()).isEqualTo("cs_late");
        assertThat(refund.amount().getAmount()).isEqualByComparingTo("17.22");
        assertThat(refund.refundedAt()).isEqualTo(NOW);
        verify(this.domainEventPublisher, never()).publish(isA(PaymentPaid.class));
        verify(this.domainEventPublisher, never()).publish(isA(PaymentRefunded.class));
    }

    @Test
    void shouldRefundOnlineCompletionOfPaymentPaidAtCounter() {
        this.paymentRepository.save(paidAtCounter());

        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_dup", "pi_dup",
                new WebhookEventId("evt_dup")));

        assertThat(payment().status()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment().method()).isEqualTo(PaymentMethod.CASH);
        verify(this.paymentGatewayServicePort).refundLateCharge("cs_dup", payment().id());
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldRefundLateChargeOnceWhenSessionIsReportedTwice() {
        this.paymentRepository.save(PaymentFixture.withStatus(PaymentStatus.CANCELED));

        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_late", "pi_late",
                new WebhookEventId("evt_1")));
        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_late", "pi_late",
                new WebhookEventId("evt_2")));

        verify(this.paymentGatewayServicePort, times(1)).refundLateCharge("cs_late", payment().id());
        assertThat(this.lateChargeRefundRepository.all()).hasSize(1);
    }

    @Test
    void shouldKeepPaymentPaidWhenGatewayRefundFails() {
        this.paymentRepository.save(paidOnline());
        final int saves = this.paymentRepository.saves();
        when(this.paymentGatewayServicePort.refund(any())).thenThrow(
                new PaymentException(PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable"));

        assertThatThrownBy(() -> this.paymentPort.requestManualRefund(payment().id()))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.GATEWAY_FAILURE));
        assertThat(payment().status()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment().refundRequestedAt()).isNull();
        assertThat(this.paymentRepository.saves()).isEqualTo(saves);
    }

    @Test
    void shouldRejectManualRefundOfUnpaidPayment() {
        this.paymentRepository.save(PaymentFixture.pending());

        assertThatThrownBy(() -> this.paymentPort.requestManualRefund(payment().id()))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.ILLEGAL_STATE));
        verifyNoInteractions(this.paymentGatewayServicePort);
    }

    @Test
    void shouldRefundImmediatelyWhenGatewayConfirmsRefund() {
        this.paymentRepository.save(paidOnline());
        when(this.paymentGatewayServicePort.refund(any()))
                .thenReturn(new GatewayRefund(RefundOutcome.COMPLETED, "pi_1"));

        this.paymentPort.requestManualRefund(payment().id());

        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(publishedRefund().getSnapshot().refundedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldIgnoreRepeatedShipmentCancellation() {
        this.paymentRepository.save(paidOnline());
        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.PLANNED);

        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.PLANNED);

        verify(this.paymentGatewayServicePort, times(1)).refund(any());
        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUND_PENDING);
    }

    @Test
    void shouldIgnoreCancellationOfShipmentWithUnknownQuote() {
        assertThatCode(() -> this.paymentPort.handleShipmentCanceled(new PriceQuoteId(999L), ShipmentStatus.CREATED))
                .doesNotThrowAnyException();
        assertThat(this.paymentRepository.all()).isEmpty();
    }

    @Test
    void shouldRejectCounterRefundOfOnlinePayment() {
        this.paymentRepository.save(paidOnline());
        this.paymentPort.requestManualRefund(payment().id());

        assertThatThrownBy(() -> this.paymentPort.confirmCounterRefund(payment().id()))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.ILLEGAL_STATE));
        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUND_PENDING);
    }

    @Test
    void shouldStoreIntentAtRefundWhenPaymentPaidWithoutIntent() {
        final Payment paid = PaymentFixture.pending();
        paid.attachCheckout(new CheckoutSession("cs_1", "https://checkout/cs_1", NOW.plusSeconds(3600)), NOW);
        paid.markPaidOnline(NOW, "cs_1", null, () -> {
        });
        this.paymentRepository.save(paid);
        when(this.paymentGatewayServicePort.refund(any()))
                .thenReturn(new GatewayRefund(RefundOutcome.PENDING, "pi_from_refund"));

        this.paymentPort.requestManualRefund(payment().id());

        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUND_PENDING);
        assertThat(payment().gatewayPaymentIntent()).isEqualTo("pi_from_refund");

        this.paymentPort.confirmGatewayRefund(new ConfirmGatewayRefundCommand("pi_from_refund",
                new WebhookEventId("evt_r2")));

        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    void shouldRefundSecondPaidSessionOfStripePaidPayment() {
        this.paymentRepository.save(paidOnline());
        final int saves = this.paymentRepository.saves();

        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_2", "pi_2",
                new WebhookEventId("evt_second")));

        verify(this.paymentGatewayServicePort).refundLateCharge("cs_2", payment().id());
        assertThat(this.lateChargeRefundRepository.all()).extracting(LateChargeRefund::gatewayReference)
                .containsExactly("cs_2");
        assertThat(payment().status()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment().gatewayPaymentIntent()).isEqualTo("pi_1");
        assertThat(payment().checkoutSession()).map(CheckoutSession::gatewayReference).contains("cs_1");
        assertThat(this.paymentRepository.saves()).isEqualTo(saves);
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldIgnoreRedeliveryOfPayingSession() {
        this.paymentRepository.save(paidOnline());
        final int saves = this.paymentRepository.saves();

        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_1", "pi_1",
                new WebhookEventId("evt_redelivered")));

        verify(this.paymentGatewayServicePort, never()).refundLateCharge(any(), any());
        assertThat(this.lateChargeRefundRepository.all()).isEmpty();
        assertThat(this.paymentRepository.saves()).isEqualTo(saves);
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldRefundOtherSessionCompletedForRefundPendingPayment() {
        this.paymentRepository.save(paidOnline());
        this.paymentPort.requestManualRefund(payment().id());

        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_2", "pi_2",
                new WebhookEventId("evt_other")));

        verify(this.paymentGatewayServicePort).refundLateCharge("cs_2", payment().id());
        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUND_PENDING);
    }

    @Test
    void shouldRefundOtherSessionCompletedForRefundedPayment() {
        this.paymentRepository.save(paidOnline());
        when(this.paymentGatewayServicePort.refund(any()))
                .thenReturn(new GatewayRefund(RefundOutcome.COMPLETED, "pi_1"));
        this.paymentPort.requestManualRefund(payment().id());

        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_2", "pi_2",
                new WebhookEventId("evt_other")));

        verify(this.paymentGatewayServicePort).refundLateCharge("cs_2", payment().id());
        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    void shouldIgnoreRedeliveryOfPayingSessionAfterRefundRequested() {
        this.paymentRepository.save(paidOnline());
        this.paymentPort.requestManualRefund(payment().id());

        assertThatCode(() -> this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(),
                "cs_1", "pi_1", new WebhookEventId("evt_redelivered")))).doesNotThrowAnyException();

        verify(this.paymentGatewayServicePort, never()).refundLateCharge(any(), any());
        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUND_PENDING);
    }

    @Test
    void shouldAskStripeToRetryChargeRefundedArrivingBeforeCommit() {
        this.paymentRepository.save(paidOnline());
        final ConfirmGatewayRefundCommand early = new ConfirmGatewayRefundCommand("pi_1",
                new WebhookEventId("evt_early"));

        assertThatThrownBy(() -> this.paymentPort.confirmGatewayRefund(early))
                .isInstanceOf(PaymentNotReadyException.class);
        assertThat(payment().status()).isEqualTo(PaymentStatus.PAID);

        this.paymentPort.requestManualRefund(payment().id());
        this.paymentPort.confirmGatewayRefund(early);

        assertThat(payment().status()).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    void shouldAcknowledgeChargeRefundedForLateChargeRefund() {
        this.paymentRepository.save(PaymentFixture.withStatus(PaymentStatus.EXPIRED));
        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(payment().id(), "cs_late", "pi_late",
                new WebhookEventId("evt_late")));
        final int saves = this.paymentRepository.saves();

        assertThatCode(() -> this.paymentPort.confirmGatewayRefund(new ConfirmGatewayRefundCommand("pi_late",
                new WebhookEventId("evt_late_refunded")))).doesNotThrowAnyException();

        assertThat(this.lateChargeRefundRepository.all()).extracting(LateChargeRefund::gatewayPaymentIntent)
                .containsExactly("pi_late");
        assertThat(payment().status()).isEqualTo(PaymentStatus.EXPIRED);
        assertThat(this.paymentRepository.saves()).isEqualTo(saves);
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldAskStripeToRetryChargeRefundedOfUnknownPaymentIntent() {
        assertThatThrownBy(() -> this.paymentPort.confirmGatewayRefund(new ConfirmGatewayRefundCommand("pi_unknown",
                new WebhookEventId("evt_unknown")))).isInstanceOf(PaymentNotReadyException.class);
    }

    @Test
    void shouldRejectCounterRefundOfPaidCounterPayment() {
        this.paymentRepository.save(paidAtCounter());

        assertThatThrownBy(() -> this.paymentPort.confirmCounterRefund(payment().id()))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.ILLEGAL_STATE));
    }

    @Test
    void shouldExpireOpenCheckoutWhenPendingPaymentCanceled() {
        final Payment pending = PaymentFixture.pending();
        pending.attachCheckout(new CheckoutSession("cs_open", "https://checkout/cs_open", NOW.plusSeconds(3600)),
                NOW);
        this.paymentRepository.save(pending);

        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.CREATED);

        verify(this.paymentGatewayServicePort).expireCheckout("cs_open");
        assertThat(payment().status()).isEqualTo(PaymentStatus.CANCELED);
    }

    @Test
    void shouldCancelPendingPaymentWhenCheckoutExpiryFails() {
        final Payment pending = PaymentFixture.pending();
        pending.attachCheckout(new CheckoutSession("cs_open", "https://checkout/cs_open", NOW.plusSeconds(3600)),
                NOW);
        this.paymentRepository.save(pending);
        when(this.paymentGatewayServicePort.expireCheckout("cs_open")).thenThrow(
                new PaymentException(PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable"));

        this.paymentPort.handleShipmentCanceled(PRICE_QUOTE_ID, ShipmentStatus.CREATED);

        assertThat(payment().status()).isEqualTo(PaymentStatus.CANCELED);
    }

    private Payment payment() {
        return this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow();
    }

    private PaymentRefunded publishedRefund() {
        final ArgumentCaptor<PaymentRefunded> event = ArgumentCaptor.forClass(PaymentRefunded.class);
        verify(this.domainEventPublisher).publish(event.capture());
        return event.getValue();
    }

    private Payment paidOnline() {
        final Payment payment = PaymentFixture.pending();
        payment.attachCheckout(new CheckoutSession("cs_1", "https://checkout/cs_1", NOW.plusSeconds(3600)), NOW);
        payment.markPaidOnline(NOW, "cs_1", "pi_1", () -> {
        });
        return payment;
    }

    private Payment paidAtCounter() {
        final Payment payment = PaymentFixture.pending();
        payment.markPaidAtCounter(PaymentMethod.CASH, USER_ID, DEPARTMENT_ID, null, NOW);
        return payment;
    }

    private static final class DirectOperatorContext implements PaymentOperatorContextServicePort {

        @Override
        public <T> T runAs(final OperatorId operatorId, final Supplier<T> operation) {
            return operation.get();
        }

        @Override
        public void runAs(final OperatorId operatorId, final Runnable operation) {
            operation.run();
        }
    }
}
