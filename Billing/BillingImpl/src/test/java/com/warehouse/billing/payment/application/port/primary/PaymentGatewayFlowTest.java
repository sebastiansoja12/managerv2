package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.application.port.primary.result.CheckoutView;
import com.warehouse.billing.payment.application.port.secondary.PaymentActorServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentDeadlineServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentOperatorContextServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentQuoteLookupServicePort;
import com.warehouse.billing.payment.application.service.LateChargeRefundService;
import com.warehouse.billing.payment.application.service.OnlineCheckoutService;
import com.warehouse.billing.payment.application.service.OnlinePaymentCompletion;
import com.warehouse.billing.payment.application.service.OnlinePaymentConfirmationService;
import com.warehouse.billing.payment.application.service.OnlinePaymentFinder;
import com.warehouse.billing.payment.application.service.PaymentExpiryService;
import com.warehouse.billing.payment.application.service.PaymentOpeningService;
import com.warehouse.billing.payment.application.service.PaymentRefundService;
import com.warehouse.billing.payment.domain.enumeration.CheckoutClosure;
import com.warehouse.billing.payment.domain.enumeration.PaymentChannel;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.event.PaymentPaid;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.service.CheckoutExpiryPolicy;
import com.warehouse.billing.payment.domain.service.PaymentFactory;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.domain.vo.PaymentQuoteReference;
import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.domain.exception.BillingCustomerException;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.customer.infrastructure.adapter.primary.BillingCustomerExceptionHandler;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.billing.payment.infrastructure.adapter.primary.ExternalPaymentController;
import com.warehouse.billing.payment.infrastructure.adapter.primary.FakeGatewayController;
import com.warehouse.billing.payment.infrastructure.adapter.primary.PaymentExceptionHandler;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentWebMapper;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway.FakePaymentGatewayClient;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.billing.pricing.domain.enumeration.PriceQuoteLineType;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.vo.PriceQuoteLine;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceListId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.WebhookEventId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.warehouse.billing.payment.domain.model.PaymentFixture.NOW;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.PRICE_QUOTE_ID;
import static com.warehouse.billing.payment.domain.model.PaymentFixture.QUOTED_AT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentGatewayFlowTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(10L);

    private static final TrackingNumber TRACKING_NUMBER = new TrackingNumber("TRK-1");

    private static final BillingCustomerId CUSTOMER_ID = new BillingCustomerId(55L);

    private static final BillingCustomerId OTHER_CUSTOMER_ID = new BillingCustomerId(56L);

    private final Map<TrackingNumber, PaymentQuoteReference> shipments = new HashMap<>();

    private InMemoryPaymentRepository paymentRepository;

    private DomainEventPublisher domainEventPublisher;

    private PaymentGatewayServicePort paymentGatewayServicePort;

    private PaymentPortImpl paymentPort;

    private BillingCustomerPort billingCustomerPort;

    @BeforeEach
    void setUp() {
        this.paymentRepository = new InMemoryPaymentRepository();
        this.billingCustomerPort = mock(BillingCustomerPort.class);
        final BillingCustomer customer = mock(BillingCustomer.class);
        when(customer.id()).thenReturn(CUSTOMER_ID);
        when(this.billingCustomerPort.currentActive()).thenReturn(customer);
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        final PricingPort pricingPort = mock(PricingPort.class);
        final PaymentDeadlineServicePort paymentDeadlineServicePort = mock(PaymentDeadlineServicePort.class);
        final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        when(paymentDeadlineServicePort.paymentDeadline()).thenReturn(Duration.ofHours(72));
        when(pricingPort.findQuote(PRICE_QUOTE_ID)).thenReturn(Optional.of(quote()));
        this.paymentGatewayServicePort = spy(new FakePaymentGatewayClient(
                "http://localhost:8080/v1/api/billing/dev/fake-gateway", new CheckoutExpiryPolicy(), clock));
        final PaymentQuoteLookupServicePort paymentQuoteLookupServicePort =
                trackingNumber -> Optional.ofNullable(this.shipments.get(trackingNumber));
        final PaymentOpeningService paymentOpeningService = new PaymentOpeningService(pricingPort,
                paymentDeadlineServicePort, new PaymentFactory(clock));
        final OnlinePaymentCompletion onlinePaymentCompletion = new OnlinePaymentCompletion(this.paymentRepository,
                this.domainEventPublisher, mock(LateChargeRefundService.class), clock);
        final OnlinePaymentFinder onlinePaymentFinder = new OnlinePaymentFinder(this.paymentRepository,
                new InMemoryLateChargeRefundRepository());
        this.paymentPort = new PaymentPortImpl(this.paymentRepository, paymentOpeningService,
                mock(PaymentActorServicePort.class), this.domainEventPublisher, clock,
                new OnlineCheckoutService(this.paymentRepository, paymentOpeningService,
                        this.paymentGatewayServicePort, onlinePaymentCompletion, clock),
                new OnlinePaymentConfirmationService(onlinePaymentFinder, new InMemoryProcessedWebhookRepository(),
                        onlinePaymentCompletion, mock(PaymentRefundService.class), clock),
                paymentQuoteLookupServicePort, new DirectOperatorContext(), onlinePaymentFinder,
                mock(PaymentExpiryService.class), mock(PaymentRefundService.class));
    }

    @Test
    void shouldReuseOpenCheckoutSession() {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));

        final CheckoutView first = this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);
        final CheckoutView second = this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);

        assertThat(first.status()).isEqualTo(PaymentStatus.PENDING);
        assertThat(first.amount().getAmount()).isEqualByComparingTo("17.22");
        assertThat(first.paymentUrl()).isNotBlank();
        assertThat(second.paymentUrl()).isEqualTo(first.paymentUrl());
        assertThat(second.expiresAt()).isEqualTo(NOW.plus(Duration.ofHours(24)));
        verify(this.paymentGatewayServicePort, times(1)).openCheckout(isA(Payment.class));
        final Payment payment = this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow();
        assertThat(payment.channel()).isEqualTo(PaymentChannel.ONLINE);
        assertThat(payment.checkoutSession()).map(CheckoutSession::url).contains(first.paymentUrl());
    }

    @Test
    void shouldOpenNewCheckoutSessionWhenStoredSessionExpired() {
        final Payment payment = pendingPayment();
        payment.attachCheckout(new CheckoutSession("cs_old", "https://checkout/old", NOW.minusSeconds(1)), NOW);
        this.paymentRepository.save(payment);
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));

        final CheckoutView view = this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);

        assertThat(view.paymentUrl()).isNotEqualTo("https://checkout/old");
        verify(this.paymentGatewayServicePort).openCheckout(payment);
    }

    @Test
    void shouldReturnStatusWithoutUrlForPaidPayment() {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));
        final CheckoutView opened = this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);
        this.paymentPort.confirmOnlinePayment(confirmation("evt_1"));

        final CheckoutView view = this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);

        assertThat(opened.paymentUrl()).isNotBlank();
        assertThat(view.status()).isEqualTo(PaymentStatus.PAID);
        assertThat(view.paymentUrl()).isNull();
        assertThat(view.expiresAt()).isNull();
    }

    @Test
    void shouldMarkPaidOnCompletedCheckout() {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));
        this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);

        this.paymentPort.confirmOnlinePayment(confirmation("evt_1"));

        final Payment payment = this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow();
        assertThat(payment.status()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment.method()).isEqualTo(PaymentMethod.STRIPE);
        assertThat(payment.channel()).isEqualTo(PaymentChannel.ONLINE);
        assertThat(payment.paidAt()).isEqualTo(NOW);
        verify(this.domainEventPublisher).publish(isA(PaymentPaid.class));
    }

    @Test
    void shouldIgnoreAlreadyProcessedWebhookEvent() {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));
        this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);
        this.paymentPort.confirmOnlinePayment(confirmation("evt_1"));
        final int savesAfterFirstDelivery = this.paymentRepository.saves();

        this.paymentPort.confirmOnlinePayment(confirmation("evt_1"));
        this.paymentPort.confirmOnlinePayment(confirmation("evt_2"));

        assertThat(this.paymentRepository.saves()).isEqualTo(savesAfterFirstDelivery);
        verify(this.domainEventPublisher, times(1)).publish(isA(PaymentPaid.class));
    }

    @Test
    void shouldRejectCheckoutWhenShipmentRequiresNoPayment() {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, false, null, CUSTOMER_ID));

        assertThatThrownBy(() -> this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID))
                .isNotInstanceOf(PaymentNotReadyException.class);
        assertThat(this.paymentRepository.all()).isEmpty();
    }

    @Test
    void shouldReturn404WithRetryHintWhenPaymentNotOpenedYet() throws Exception {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, null, CUSTOMER_ID));

        externalApi().perform(get("/external/payments/TRK-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.retryAfterSeconds").value(2));
        assertThat(this.paymentRepository.all()).isEmpty();
    }

    @Test
    void shouldReturn404WhenShipmentIsUnknown() throws Exception {
        externalApi().perform(get("/external/payments/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.retryAfterSeconds").doesNotExist());
    }

    @Test
    void shouldHideShipmentOfAnotherBillingCustomer() throws Exception {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID,
                OTHER_CUSTOMER_ID));

        externalApi().perform(get("/external/payments/TRK-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Shipment was not found"));
        assertThat(this.paymentRepository.all()).isEmpty();
    }

    @Test
    void shouldHideShipmentCreatedWithoutBillingCustomer() {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, null));

        assertThatThrownBy(() -> this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID))
                .isInstanceOf(PaymentException.class)
                .hasMessage("Shipment was not found");
        assertThat(this.paymentRepository.all()).isEmpty();
    }

    @Test
    void shouldRejectExternalPaymentReadByBlockedCustomer() throws Exception {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID,
                CUSTOMER_ID));
        when(this.billingCustomerPort.currentActive()).thenThrow(new BillingCustomerException(
                BillingCustomerException.Reason.NOT_ACTIVE, "Business customer is not active"));

        externalApi().perform(get("/external/payments/TRK-1"))
                .andExpect(status().isForbidden());
        assertThat(this.paymentRepository.all()).isEmpty();
    }

    @Test
    void shouldOpenPaymentFromQuoteOnFirstExternalCall() throws Exception {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));

        externalApi().perform(get("/external/payments/TRK-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.amount").value(17.22))
                .andExpect(jsonPath("$.currency").value("PLN"))
                .andExpect(jsonPath("$.paymentUrl").isNotEmpty())
                .andExpect(jsonPath("$.expiresAt").isNotEmpty());
        assertThat(this.paymentRepository.all()).hasSize(1);
    }

    @Test
    void shouldCompleteFakeCheckoutOpenedInBrowser() throws Exception {
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));
        this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);
        final PaymentId paymentId = this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow().id();
        final FormattingConversionService conversion = new FormattingConversionService();
        conversion.addConverter(String.class, PaymentId.class, value -> new PaymentId(Long.valueOf(value)));
        final MockMvc fakeGateway = MockMvcBuilders
                .standaloneSetup(new FakeGatewayController(this.paymentPort, new PaymentWebMapper()))
                .setControllerAdvice(new PaymentExceptionHandler())
                .setConversionService(conversion)
                .build();

        fakeGateway.perform(get("/billing/dev/fake-gateway/" + paymentId.value() + "/COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
        verify(this.domainEventPublisher).publish(isA(PaymentPaid.class));
    }

    private MockMvc externalApi() {
        return MockMvcBuilders
                .standaloneSetup(new ExternalPaymentController(this.paymentPort, this.billingCustomerPort,
                        new PaymentWebMapper()))
                .setControllerAdvice(new PaymentExceptionHandler(), new BillingCustomerExceptionHandler())
                .build();
    }

    @Test
    void shouldConfirmPaymentFromMetadataForSupersededSession() {
        this.paymentRepository.save(pendingPaymentWithExpiredSession());
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));
        this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);

        this.paymentPort.confirmOnlinePayment(new ConfirmOnlinePaymentCommand(new PaymentId(1L), "cs_old", null,
                new WebhookEventId("evt_late")));

        final Payment payment = this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow();
        assertThat(payment.checkoutSession()).map(CheckoutSession::gatewayReference).contains("cs_old");
        assertThat(payment.status()).isEqualTo(PaymentStatus.PAID);
        verify(this.domainEventPublisher).publish(isA(PaymentPaid.class));
    }

    @Test
    void shouldExpireOldSessionBeforeReplacingIt() {
        final Payment payment = pendingPaymentWithExpiredSession();
        this.paymentRepository.save(payment);
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));

        this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);

        final InOrder gateway = inOrder(this.paymentGatewayServicePort);
        gateway.verify(this.paymentGatewayServicePort).expireCheckout("cs_old");
        gateway.verify(this.paymentGatewayServicePort).openCheckout(payment);
    }

    @Test
    void shouldConfirmPaymentInsteadOfReplacingCompletedSession() {
        this.paymentRepository.save(pendingPaymentWithExpiredSession());
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));
        doReturn(CheckoutClosure.COMPLETED).when(this.paymentGatewayServicePort).expireCheckout("cs_old");

        final CheckoutView view = this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);

        assertThat(view.status()).isEqualTo(PaymentStatus.PAID);
        assertThat(view.paymentUrl()).isNull();
        verify(this.paymentGatewayServicePort, never()).openCheckout(any());
        assertThat(this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow().checkoutSession())
                .map(CheckoutSession::gatewayReference).contains("cs_old");
        verify(this.domainEventPublisher).publish(isA(PaymentPaid.class));
    }

    @Test
    void shouldNotOpenCheckoutAfterDeadline() {
        this.paymentRepository.save(new Payment(new PaymentId(1L), PRICE_QUOTE_ID,
                Money.from(new BigDecimal("17.22"), Currency.PLN), null, null, PaymentStatus.PENDING, null, 0, NOW,
                QUOTED_AT, null, null, null, null, null, null, null, null, 0L));
        this.shipments.put(TRACKING_NUMBER, new PaymentQuoteReference(OPERATOR_ID, true, PRICE_QUOTE_ID, CUSTOMER_ID));

        final CheckoutView view = this.paymentPort.openOnlineCheckout(TRACKING_NUMBER, CUSTOMER_ID);

        assertThat(view.status()).isEqualTo(PaymentStatus.PENDING);
        assertThat(view.paymentUrl()).isNull();
        assertThat(view.expiresAt()).isNull();
        verify(this.paymentGatewayServicePort, never()).openCheckout(any());
    }

    @Test
    void shouldRejectConfirmationOfUnknownPayment() {
        assertThatThrownBy(() -> this.paymentPort.confirmOnlinePayment(
                new ConfirmOnlinePaymentCommand(new PaymentId(999L), null, null, new WebhookEventId("evt_unknown"))))
                .isInstanceOf(PaymentException.class);
    }

    private Payment pendingPaymentWithExpiredSession() {
        final Payment payment = pendingPayment();
        payment.attachCheckout(new CheckoutSession("cs_old", "https://checkout/old", NOW.minusSeconds(1)), NOW);
        return payment;
    }

    private ConfirmOnlinePaymentCommand confirmation(final String eventId) {
        final Payment payment = this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow();
        return new ConfirmOnlinePaymentCommand(payment.id(), gatewayReference(), null,
                new WebhookEventId(eventId));
    }

    private String gatewayReference() {
        return this.paymentRepository.findByPriceQuote(PRICE_QUOTE_ID).orElseThrow().checkoutSession()
                .map(CheckoutSession::gatewayReference).orElseThrow();
    }

    private Payment pendingPayment() {
        return new Payment(new PaymentId(1L), PRICE_QUOTE_ID, Money.from(new BigDecimal("17.22"), Currency.PLN),
                null, null, PaymentStatus.PENDING, null, 0, QUOTED_AT.plus(Duration.ofHours(72)), NOW, null, null,
                null, null, null, null, null, null, 0L);
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

    private static final class DirectOperatorContext implements PaymentOperatorContextServicePort {

        @Override
        public <T> T runAs(final OperatorId operatorId, final Supplier<T> operation) {
            assertThat(operatorId).isEqualTo(OPERATOR_ID);
            return operation.get();
        }

        @Override
        public void runAs(final OperatorId operatorId, final Runnable operation) {
            assertThat(operatorId).isEqualTo(OPERATOR_ID);
            operation.run();
        }
    }
}
