package com.warehouse.billing.invoice.application.port.primary;

import com.warehouse.billing.invoice.application.port.primary.command.IssueCorrectionCommand;
import com.warehouse.billing.invoice.application.port.primary.command.IssueInvoiceCommand;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceBuyerServicePort;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceDocumentServicePort;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceSellerServicePort;
import com.warehouse.billing.invoice.application.service.CorrectionInvoiceIssuer;
import com.warehouse.billing.invoice.application.service.InvoiceIssuer;
import com.warehouse.billing.invoice.application.service.InvoiceRegistration;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.event.InvoiceIssued;
import com.warehouse.billing.invoice.domain.exception.InvoiceException;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.service.InvoiceFactory;
import com.warehouse.billing.invoice.domain.service.InvoiceNumberFormatter;
import com.warehouse.billing.invoice.domain.vo.InvoiceLine;
import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.domain.enumeration.PaymentChannel;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.BUYER_TAX_ID;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.DOCUMENT_ID;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.PAID_AT;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.PAYMENT_ID;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.PRICE_QUOTE_ID;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.REFUNDED_AT;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.USER_ID;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.buyer;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.money;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.paidAtCounter;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.quote;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.seller;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InvoicePortImplTest {

    private static final OperatorId OPERATOR_A = OperatorId.of(10L);

    private static final OperatorId OPERATOR_B = OperatorId.of(11L);

    private final AtomicReference<OperatorId> currentOperator = new AtomicReference<>(OPERATOR_A);

    private InMemoryInvoiceRepository invoiceRepository;

    private PaymentPort paymentPort;

    private PricingPort pricingPort;

    private InvoiceDocumentServicePort invoiceDocumentServicePort;

    private DomainEventPublisher domainEventPublisher;

    private InvoicePortImpl invoicePort;

    @BeforeEach
    void setUp() {
        this.invoiceRepository = new InMemoryInvoiceRepository();
        final InMemoryInvoiceNumberSequenceRepository sequenceRepository =
                new InMemoryInvoiceNumberSequenceRepository(this.currentOperator::get);
        this.paymentPort = mock(PaymentPort.class);
        this.pricingPort = mock(PricingPort.class);
        this.invoiceDocumentServicePort = mock(InvoiceDocumentServicePort.class);
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        final InvoiceSellerServicePort sellerServicePort = mock(InvoiceSellerServicePort.class);
        final InvoiceBuyerServicePort buyerServicePort = mock(InvoiceBuyerServicePort.class);
        final Clock clock = Clock.fixed(Instant.parse("2026-11-24T12:05:00Z"), ZoneOffset.UTC);
        when(sellerServicePort.currentSeller()).thenReturn(seller());
        when(buyerServicePort.findBuyer(any(PriceQuoteId.class), eq(BUYER_TAX_ID))).thenReturn(buyer());
        when(this.invoiceDocumentServicePort.store(any(Invoice.class), any())).thenReturn(DOCUMENT_ID);
        final InvoiceFactory invoiceFactory = new InvoiceFactory(new InvoiceNumberFormatter(),
                ZoneId.of("Europe/Warsaw"), clock);
        final InvoiceRegistration invoiceRegistration = new InvoiceRegistration(this.invoiceRepository,
                this.invoiceDocumentServicePort, this.domainEventPublisher, clock);
        final InvoiceIssuer invoiceIssuer = new InvoiceIssuer(this.invoiceRepository, sequenceRepository,
                this.paymentPort, this.pricingPort, sellerServicePort, buyerServicePort, invoiceFactory,
                invoiceRegistration);
        this.invoicePort = new InvoicePortImpl(invoiceIssuer,
                new CorrectionInvoiceIssuer(this.invoiceRepository, sequenceRepository, this.paymentPort,
                        invoiceFactory, invoiceRegistration, invoiceIssuer),
                this.invoiceRepository, this.invoiceDocumentServicePort);
        payment(PAYMENT_ID, PRICE_QUOTE_ID, PAID_AT);
    }

    @Test
    void shouldIssueInvoiceFromQuoteLinesWhenShipmentPaid() {
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID));

        final Invoice invoice = this.invoiceRepository.findByPayment(PAYMENT_ID, InvoiceType.INVOICE).orElseThrow();
        assertThat(invoice.number()).isEqualTo("FV/2026/11/0001");
        assertThat(invoice.priceQuoteId()).isEqualTo(PRICE_QUOTE_ID);
        assertThat(invoice.paymentMethod()).isEqualTo(PaymentMethod.CASH);
        assertThat(invoice.issueDate()).isEqualTo(LocalDate.of(2026, 11, 24));
        assertThat(invoice.saleDate()).isEqualTo(LocalDate.of(2026, 11, 24));
        assertThat(invoice.correctedInvoiceId()).isNull();
        assertThat(invoice.lines()).extracting(InvoiceLine::description)
                .containsExactly("Przesyłka standardowa", "Ubezpieczenie");
        assertThat(invoice.lines().get(0).gross().getAmount()).isEqualByComparingTo("18.45");
        assertThat(invoice.lines().get(0).vatPercent()).isEqualByComparingTo("23");
        assertThat(invoice.net().getAmount()).isEqualByComparingTo("20.00");
        assertThat(invoice.vat().getAmount()).isEqualByComparingTo("4.60");
        assertThat(invoice.gross().getAmount()).isEqualByComparingTo("24.60");
        assertThat(invoice.seller().name()).isEqualTo("Szybka Paczka Sp. z o.o.");
        assertThat(invoice.sellerBankAccount()).isEqualTo("PL61109010140000071219812874");
        assertThat(invoice.buyer().taxId()).isEqualTo(BUYER_TAX_ID);
        assertThat(invoice.buyerLanguage()).isEqualTo("pl");
        assertThat(invoice.documentId()).contains(DOCUMENT_ID);
        verify(this.invoiceDocumentServicePort).store(invoice, USER_ID);
        verify(this.domainEventPublisher).publish(isA(InvoiceIssued.class));
    }

    @Test
    void shouldIssueSingleInvoicePerPayment() {
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID));
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID));

        assertThat(this.invoiceRepository.all()).hasSize(1);
        verify(this.invoiceDocumentServicePort, times(1)).store(any(Invoice.class), any());
        verify(this.domainEventPublisher, times(1)).publish(isA(InvoiceIssued.class));
    }

    @Test
    void shouldIssueCorrectionWithNegatedLinesOnRefund() {
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID));
        final Invoice original = this.invoiceRepository.findByPayment(PAYMENT_ID, InvoiceType.INVOICE)
                .orElseThrow();

        this.invoicePort.issueCorrection(new IssueCorrectionCommand(PAYMENT_ID, REFUNDED_AT));
        this.invoicePort.issueCorrection(new IssueCorrectionCommand(PAYMENT_ID, REFUNDED_AT));

        final Invoice correction = this.invoiceRepository.findByPayment(PAYMENT_ID, InvoiceType.CORRECTION)
                .orElseThrow();
        assertThat(this.invoiceRepository.all()).hasSize(2);
        assertThat(correction.number()).isEqualTo("KOR/2026/12/0001");
        assertThat(correction.correctedInvoiceId()).isEqualTo(original.id());
        assertThat(correction.issueDate()).isEqualTo(LocalDate.of(2026, 12, 1));
        assertThat(correction.saleDate()).isEqualTo(original.saleDate());
        assertThat(correction.buyer()).isEqualTo(original.buyer());
        assertThat(correction.lines()).extracting(InvoiceLine::description)
                .containsExactly("Przesyłka standardowa", "Ubezpieczenie");
        assertThat(correction.lines().get(0).net().getAmount()).isEqualByComparingTo("-15.00");
        assertThat(correction.lines().get(1).vat().getAmount()).isEqualByComparingTo("-1.15");
        assertThat(correction.gross().getAmount()).isEqualByComparingTo("-24.60");
        assertThat(correction.documentId()).contains(DOCUMENT_ID);
        verify(this.domainEventPublisher, times(2)).publish(isA(InvoiceIssued.class));
    }

    @Test
    void shouldIssueOriginalInlineWhenRefundArrivesFirst() {
        this.invoicePort.issueCorrection(new IssueCorrectionCommand(PAYMENT_ID, REFUNDED_AT));
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID));

        final Invoice original = this.invoiceRepository.findByPayment(PAYMENT_ID, InvoiceType.INVOICE)
                .orElseThrow();
        final Invoice correction = this.invoiceRepository.findByPayment(PAYMENT_ID, InvoiceType.CORRECTION)
                .orElseThrow();
        assertThat(this.invoiceRepository.all()).hasSize(2);
        assertThat(original.number()).isEqualTo("FV/2026/11/0001");
        assertThat(correction.number()).isEqualTo("KOR/2026/12/0001");
        assertThat(correction.correctedInvoiceId()).isEqualTo(original.id());
        assertThat(correction.gross().getAmount()).isEqualByComparingTo("-24.60");
        verify(this.domainEventPublisher, times(2)).publish(isA(InvoiceIssued.class));
    }

    @Test
    void shouldNotDuplicateOriginalWhenInvoiceEventArrivesAfterInlineIssue() {
        this.invoicePort.issueCorrection(new IssueCorrectionCommand(PAYMENT_ID, REFUNDED_AT));
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID));
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID));
        this.invoicePort.issueCorrection(new IssueCorrectionCommand(PAYMENT_ID, REFUNDED_AT));

        assertThat(this.invoiceRepository.all()).extracting(Invoice::type)
                .containsExactly(InvoiceType.INVOICE, InvoiceType.CORRECTION);
        verify(this.invoiceDocumentServicePort, times(2)).store(any(Invoice.class), any());
        verify(this.domainEventPublisher, times(2)).publish(isA(InvoiceIssued.class));
    }

    @Test
    void shouldRejectInvoiceWhenQuoteGrossDiffersFromPaidAmount() {
        when(this.paymentPort.findById(PAYMENT_ID)).thenReturn(Optional.of(new Payment(PAYMENT_ID, PRICE_QUOTE_ID,
                money("20.00"), PaymentChannel.COUNTER, PaymentMethod.CASH, PaymentStatus.PAID, null, 0,
                PAID_AT.plusSeconds(3600), PAID_AT, PAID_AT, USER_ID, null, BUYER_TAX_ID, null, null, null, null,
                1L)));

        assertThatThrownBy(() -> this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID)))
                .isInstanceOfSatisfying(InvoiceException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(InvoiceException.Reason.ILLEGAL_STATE));

        assertThat(this.invoiceRepository.all()).isEmpty();
    }

    @Test
    void shouldIncrementSequencePerOperatorAndMonth() {
        payment(new PaymentId(2L), new PriceQuoteId(21L), PAID_AT.plusSeconds(60));
        payment(new PaymentId(3L), new PriceQuoteId(22L), Instant.parse("2026-11-30T23:30:00Z"));
        payment(new PaymentId(4L), new PriceQuoteId(23L), PAID_AT);

        this.invoicePort.issueForPayment(new IssueInvoiceCommand(PAYMENT_ID));
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(new PaymentId(2L)));
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(new PaymentId(3L)));
        this.currentOperator.set(OPERATOR_B);
        this.invoicePort.issueForPayment(new IssueInvoiceCommand(new PaymentId(4L)));

        assertThat(this.invoiceRepository.all()).extracting(Invoice::number)
                .containsExactly("FV/2026/11/0001", "FV/2026/11/0002", "FV/2026/12/0001", "FV/2026/11/0001");
    }

    private void payment(final PaymentId paymentId, final PriceQuoteId priceQuoteId, final Instant paidAt) {
        when(this.paymentPort.findById(paymentId)).thenReturn(Optional.of(paidAtCounter(paymentId, priceQuoteId,
                paidAt)));
        when(this.pricingPort.findQuote(priceQuoteId)).thenReturn(Optional.of(quote(priceQuoteId)));
    }
}
