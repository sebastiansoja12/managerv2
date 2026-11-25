package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.secondary.PaymentActorServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentOperatorContextServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentQuoteLookupServicePort;
import com.warehouse.billing.payment.application.service.OnlineCheckoutService;
import com.warehouse.billing.payment.application.service.LateChargeRefundService;
import com.warehouse.billing.payment.application.service.OnlinePaymentCompletion;
import com.warehouse.billing.payment.application.service.OnlinePaymentConfirmationService;
import com.warehouse.billing.payment.application.service.OnlinePaymentFinder;
import com.warehouse.billing.payment.application.service.PaymentExpiration;
import com.warehouse.billing.payment.application.service.PaymentExpiryService;
import com.warehouse.billing.payment.application.service.PaymentOpeningService;
import com.warehouse.billing.payment.application.service.PaymentRefundService;
import com.warehouse.billing.payment.domain.enumeration.CheckoutClosure;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.event.PaymentExpired;
import com.warehouse.billing.payment.domain.event.PaymentPaid;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.model.PaymentFixture;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static com.warehouse.billing.payment.domain.model.PaymentFixture.QUOTED_AT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentExpiryTest {

    private static final Instant DEADLINE = Instant.parse("2026-11-27T08:00:00Z");

    private static final Instant EXPIRY_TIME = Instant.parse("2026-11-27T08:15:00Z");

    private InMemoryPaymentRepository paymentRepository;

    private DomainEventPublisher domainEventPublisher;

    private PaymentGatewayServicePort paymentGatewayServicePort;

    private PaymentPortImpl paymentPort;

    @BeforeEach
    void setUp() {
        this.paymentRepository = new InMemoryPaymentRepository();
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        this.paymentGatewayServicePort = mock(PaymentGatewayServicePort.class);
        final Clock clock = Clock.fixed(EXPIRY_TIME, ZoneOffset.UTC);
        final OnlinePaymentCompletion onlinePaymentCompletion = new OnlinePaymentCompletion(this.paymentRepository,
                this.domainEventPublisher, mock(LateChargeRefundService.class), clock);
        final PaymentExpiryService paymentExpiryService = new PaymentExpiryService(this.paymentRepository,
                this.paymentGatewayServicePort, onlinePaymentCompletion,
                new PaymentExpiration(this.paymentRepository, this.domainEventPublisher),
                new TransactionTemplate(mock(PlatformTransactionManager.class)));
        this.paymentPort = new PaymentPortImpl(this.paymentRepository, mock(PaymentOpeningService.class),
                mock(PaymentActorServicePort.class), this.domainEventPublisher, clock,
                mock(OnlineCheckoutService.class), mock(OnlinePaymentConfirmationService.class),
                mock(PaymentQuoteLookupServicePort.class), mock(PaymentOperatorContextServicePort.class),
                mock(OnlinePaymentFinder.class), paymentExpiryService, mock(PaymentRefundService.class));
    }

    @Test
    void shouldExpireOverduePendingPayment() {
        this.paymentRepository.save(PaymentFixture.pendingDue(new PaymentId(1L), new PriceQuoteId(20L), DEADLINE));

        this.paymentPort.expireOverdue(EXPIRY_TIME);

        final Payment payment = this.paymentRepository.findById(new PaymentId(1L)).orElseThrow();
        assertThat(payment.status()).isEqualTo(PaymentStatus.EXPIRED);
        final ArgumentCaptor<PaymentExpired> event = ArgumentCaptor.forClass(PaymentExpired.class);
        verify(this.domainEventPublisher).publish(event.capture());
        assertThat(event.getValue().getSnapshot().paymentId()).isEqualTo(new PaymentId(1L));
        assertThat(event.getValue().getSnapshot().priceQuoteId()).isEqualTo(new PriceQuoteId(20L));
        assertThat(event.getValue().getSnapshot().status()).isEqualTo(PaymentStatus.EXPIRED);
        assertThat(event.getValue().getTimestamp()).isEqualTo(EXPIRY_TIME);
        verify(this.paymentGatewayServicePort, never()).expireCheckout(anyString());
    }

    @Test
    void shouldNotExpirePaidPayment() {
        final Payment paid = PaymentFixture.pendingDue(new PaymentId(1L), new PriceQuoteId(20L), DEADLINE);
        paid.markPaid(PaymentMethod.CASH, PaymentFixture.USER_ID, PaymentFixture.DEPARTMENT_ID, QUOTED_AT);
        this.paymentRepository.save(paid);

        this.paymentPort.expireOverdue(EXPIRY_TIME);

        assertThat(this.paymentRepository.findById(new PaymentId(1L)).orElseThrow().status())
                .isEqualTo(PaymentStatus.PAID);
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldNotExpirePendingPaymentBeforeDeadline() {
        this.paymentRepository.save(PaymentFixture.pendingDue(new PaymentId(1L), new PriceQuoteId(20L),
                EXPIRY_TIME.plusSeconds(60)));

        this.paymentPort.expireOverdue(EXPIRY_TIME);

        assertThat(this.paymentRepository.findById(new PaymentId(1L)).orElseThrow().status())
                .isEqualTo(PaymentStatus.PENDING);
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldExpireStripeCheckoutWhenPaymentExpires() {
        this.paymentRepository.save(pendingWithCheckout(new PaymentId(1L), new PriceQuoteId(20L), "cs_open"));
        when(this.paymentGatewayServicePort.expireCheckout("cs_open")).thenReturn(CheckoutClosure.EXPIRED);

        this.paymentPort.expireOverdue(EXPIRY_TIME);

        final InOrder order = inOrder(this.paymentGatewayServicePort, this.domainEventPublisher);
        order.verify(this.paymentGatewayServicePort).expireCheckout("cs_open");
        order.verify(this.domainEventPublisher).publish(isA(PaymentExpired.class));
        assertThat(this.paymentRepository.findById(new PaymentId(1L)).orElseThrow().status())
                .isEqualTo(PaymentStatus.EXPIRED);
    }

    @Test
    void shouldConfirmInsteadOfExpiringWhenCheckoutCompleted() {
        this.paymentRepository.save(pendingWithCheckout(new PaymentId(1L), new PriceQuoteId(20L), "cs_paid"));
        when(this.paymentGatewayServicePort.expireCheckout("cs_paid")).thenReturn(CheckoutClosure.COMPLETED);

        this.paymentPort.expireOverdue(EXPIRY_TIME);

        final Payment payment = this.paymentRepository.findById(new PaymentId(1L)).orElseThrow();
        assertThat(payment.status()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment.method()).isEqualTo(PaymentMethod.STRIPE);
        verify(this.domainEventPublisher).publish(isA(PaymentPaid.class));
        verify(this.domainEventPublisher, never()).publish(isA(PaymentExpired.class));
    }

    @Test
    void shouldContinueWithNextPaymentWhenOnePaymentFails() {
        this.paymentRepository.save(pendingWithCheckout(new PaymentId(1L), new PriceQuoteId(20L), "cs_broken"));
        this.paymentRepository.save(PaymentFixture.pendingDue(new PaymentId(2L), new PriceQuoteId(21L),
                DEADLINE.plusSeconds(60)));
        when(this.paymentGatewayServicePort.expireCheckout("cs_broken"))
                .thenThrow(new PaymentException(PaymentException.Reason.GATEWAY_FAILURE, "Gateway is unavailable"));

        this.paymentPort.expireOverdue(EXPIRY_TIME);

        assertThat(this.paymentRepository.findById(new PaymentId(1L)).orElseThrow().status())
                .isEqualTo(PaymentStatus.PENDING);
        assertThat(this.paymentRepository.findById(new PaymentId(2L)).orElseThrow().status())
                .isEqualTo(PaymentStatus.EXPIRED);
    }

    private Payment pendingWithCheckout(final PaymentId paymentId, final PriceQuoteId priceQuoteId,
                                        final String gatewayReference) {
        final Payment payment = PaymentFixture.pendingDue(paymentId, priceQuoteId, DEADLINE);
        payment.attachCheckout(new CheckoutSession(gatewayReference, "https://checkout/" + gatewayReference,
                DEADLINE), QUOTED_AT);
        return payment;
    }
}
