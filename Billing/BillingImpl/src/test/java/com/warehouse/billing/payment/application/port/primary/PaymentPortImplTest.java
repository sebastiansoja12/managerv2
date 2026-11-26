package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.primary.command.FindPaymentsCommand;
import com.warehouse.billing.payment.application.port.primary.command.OpenPaymentCommand;
import com.warehouse.billing.payment.application.port.primary.command.RecordCounterPaymentCommand;
import com.warehouse.billing.payment.application.port.secondary.PaymentActorServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentDeadlineServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentOperatorContextServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentQuoteLookupServicePort;
import com.warehouse.billing.payment.application.service.OnlineCheckoutService;
import com.warehouse.billing.payment.application.service.OnlinePaymentConfirmationService;
import com.warehouse.billing.payment.application.service.OnlinePaymentFinder;
import com.warehouse.billing.payment.application.service.PaymentExpiryService;
import com.warehouse.billing.payment.application.service.PaymentOpeningService;
import com.warehouse.billing.payment.application.service.PaymentRefundService;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.event.PaymentPaid;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.service.PaymentFactory;
import com.warehouse.billing.payment.domain.vo.PaymentActor;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.billing.pricing.domain.enumeration.PriceQuoteLineType;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.vo.PriceQuoteLine;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.PriceListId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static com.warehouse.billing.payment.domain.model.PaymentFixture.DEPARTMENT_ID;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.NOW;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.PRICE_QUOTE_ID;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.QUOTED_AT;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentPortImplTest {

    private InMemoryPaymentRepository paymentRepository;

    private PricingPort pricingPort;

    private DomainEventPublisher domainEventPublisher;

    private PaymentPortImpl paymentPort;

    @BeforeEach
    void setUp() {
        this.paymentRepository = new InMemoryPaymentRepository();
        this.pricingPort = mock(PricingPort.class);
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        final PaymentDeadlineServicePort paymentDeadlineServicePort = mock(PaymentDeadlineServicePort.class);
        final PaymentActorServicePort paymentActorServicePort = mock(PaymentActorServicePort.class);
        final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        when(paymentDeadlineServicePort.paymentDeadline()).thenReturn(Duration.ofHours(72));
        when(this.pricingPort.findQuote(PRICE_QUOTE_ID)).thenReturn(Optional.of(quote()));
        when(paymentActorServicePort.currentActor()).thenReturn(new PaymentActor(USER_ID, DEPARTMENT_ID));
        this.paymentPort = new PaymentPortImpl(this.paymentRepository,
                new PaymentOpeningService(this.pricingPort, paymentDeadlineServicePort, new PaymentFactory(clock)),
                paymentActorServicePort, this.domainEventPublisher, clock, mock(OnlineCheckoutService.class),
                mock(OnlinePaymentConfirmationService.class), mock(PaymentQuoteLookupServicePort.class),
                mock(PaymentOperatorContextServicePort.class), mock(OnlinePaymentFinder.class),
                mock(PaymentExpiryService.class), mock(PaymentRefundService.class));
    }

    @Test
    void shouldOpenPendingPaymentFromQuote() {
        this.paymentPort.open(new OpenPaymentCommand(PRICE_QUOTE_ID, true));

        final Payment payment = this.paymentPort.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow();
        assertThat(payment.status()).isEqualTo(PaymentStatus.PENDING);
        assertThat(payment.amount().getAmount()).isEqualByComparingTo("17.22");
        assertThat(payment.amount().getCurrency()).isEqualTo(Currency.PLN);
        assertThat(payment.priceQuoteId()).isEqualTo(PRICE_QUOTE_ID);
        assertThat(payment.createdAt()).isEqualTo(NOW);
    }

    @Test
    void shouldSetDeadlineFromOperatorConfiguration() {
        this.paymentPort.open(new OpenPaymentCommand(PRICE_QUOTE_ID, true));

        assertThat(this.paymentPort.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow().deadline())
                .isEqualTo(QUOTED_AT.plus(Duration.ofHours(72)));
    }

    @Test
    void shouldIgnoreShipmentCreatedWithPaymentNotRequired() {
        this.paymentPort.open(new OpenPaymentCommand(null, false));

        assertThat(this.paymentRepository.all()).isEmpty();
        verify(this.pricingPort, never()).findQuote(any());
    }

    @Test
    void shouldNotOpenPaymentWhenQuoteIsMissing() {
        when(this.pricingPort.findQuote(PRICE_QUOTE_ID)).thenReturn(Optional.empty());

        this.paymentPort.open(new OpenPaymentCommand(PRICE_QUOTE_ID, true));

        assertThat(this.paymentRepository.all()).isEmpty();
    }

    @Test
    void shouldOpenPaymentOnlyOnceForRepeatedShipmentCreatedEvent() {
        this.paymentPort.open(new OpenPaymentCommand(PRICE_QUOTE_ID, true));

        this.paymentPort.open(new OpenPaymentCommand(PRICE_QUOTE_ID, true));

        assertThat(this.paymentRepository.all()).hasSize(1);
        assertThat(this.paymentRepository.saves()).isEqualTo(1);
    }

    @Test
    void shouldOpenPaymentWhenCounterPaymentArrivesBeforeShipmentCreatedEvent() {
        final Payment payment = this.paymentPort.recordCounterPayment(
                new RecordCounterPaymentCommand(PRICE_QUOTE_ID, PaymentMethod.CASH, TaxId.of("5260250995")));
        this.paymentPort.open(new OpenPaymentCommand(PRICE_QUOTE_ID, true));

        assertThat(this.paymentRepository.all()).containsExactly(payment);
        assertThat(payment.status()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment.amount().getAmount()).isEqualByComparingTo("17.22");
        assertThat(payment.deadline()).isEqualTo(QUOTED_AT.plus(Duration.ofHours(72)));
        assertThat(payment.recordedBy()).isEqualTo(USER_ID);
        assertThat(payment.department()).isEqualTo(DEPARTMENT_ID);
        assertThat(payment.buyerTaxId()).isEqualTo(TaxId.of("5260250995"));
        verify(this.domainEventPublisher).publish(isA(PaymentPaid.class));
    }

    @Test
    void shouldIgnoreSecondCounterPaymentForPaidQuote() {
        this.paymentPort.open(new OpenPaymentCommand(PRICE_QUOTE_ID, true));
        final RecordCounterPaymentCommand command = new RecordCounterPaymentCommand(PRICE_QUOTE_ID,
                PaymentMethod.CARD, null);
        this.paymentPort.recordCounterPayment(command);

        this.paymentPort.recordCounterPayment(command);

        assertThat(this.paymentPort.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow().status())
                .isEqualTo(PaymentStatus.PAID);
        verify(this.domainEventPublisher, times(1)).publish(isA(PaymentPaid.class));
    }

    @Test
    void shouldRejectStripeMethodForCounterPayment() {
        this.paymentPort.open(new OpenPaymentCommand(PRICE_QUOTE_ID, true));

        assertThatThrownBy(() -> this.paymentPort.recordCounterPayment(
                new RecordCounterPaymentCommand(PRICE_QUOTE_ID, PaymentMethod.STRIPE, null)))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.INVALID_REQUEST));
        assertThat(this.paymentPort.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow().status())
                .isEqualTo(PaymentStatus.PENDING);
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldReturnNotFoundForCounterPaymentOfUnknownQuote() {
        when(this.pricingPort.findQuote(PRICE_QUOTE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.paymentPort.recordCounterPayment(
                new RecordCounterPaymentCommand(PRICE_QUOTE_ID, PaymentMethod.CASH, null)))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.NOT_FOUND));
        assertThat(this.paymentRepository.all()).isEmpty();
    }

    @Test
    void shouldRejectUnboundedPaging() {
        assertThatThrownBy(() -> new FindPaymentsCommand(null, null, -1, 20)).isInstanceOf(PaymentException.class);
        assertThatThrownBy(() -> new FindPaymentsCommand(null, null, 0, 0)).isInstanceOf(PaymentException.class);
        assertThatThrownBy(() -> new FindPaymentsCommand(null, null, 0, 101)).isInstanceOf(PaymentException.class);
    }

    private PriceQuote quote() {
        return new PriceQuote(PRICE_QUOTE_ID, new PriceListId(1L), Currency.PLN,
                List.of(new PriceQuoteLine(PriceQuoteLineType.BASE, "Base", pln("14.00"), pln("3.22"),
                        pln("17.22"))),
                new BigDecimal("23.000"), QUOTED_AT);
    }

    private Money pln(final String amount) {
        return Money.from(new BigDecimal(amount), Currency.PLN);
    }
}
