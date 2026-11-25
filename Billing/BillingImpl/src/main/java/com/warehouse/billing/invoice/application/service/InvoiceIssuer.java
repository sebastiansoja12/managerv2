package com.warehouse.billing.invoice.application.service;

import com.warehouse.billing.invoice.application.port.secondary.InvoiceBuyerServicePort;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceNumberSequenceRepository;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceSellerServicePort;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.exception.InvoiceException;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.service.InvoiceFactory;
import com.warehouse.billing.invoice.domain.vo.InvoiceDraft;
import com.warehouse.billing.invoice.domain.vo.InvoiceLine;
import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.commonassets.identificator.PaymentId;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class InvoiceIssuer {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceNumberSequenceRepository invoiceNumberSequenceRepository;

    private final PaymentPort paymentPort;

    private final PricingPort pricingPort;

    private final InvoiceSellerServicePort invoiceSellerServicePort;

    private final InvoiceBuyerServicePort invoiceBuyerServicePort;

    private final InvoiceFactory invoiceFactory;

    private final InvoiceRegistration invoiceRegistration;

    public InvoiceIssuer(final InvoiceRepository invoiceRepository,
                         final InvoiceNumberSequenceRepository invoiceNumberSequenceRepository,
                         final PaymentPort paymentPort,
                         final PricingPort pricingPort,
                         final InvoiceSellerServicePort invoiceSellerServicePort,
                         final InvoiceBuyerServicePort invoiceBuyerServicePort,
                         final InvoiceFactory invoiceFactory,
                         final InvoiceRegistration invoiceRegistration) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceNumberSequenceRepository = invoiceNumberSequenceRepository;
        this.paymentPort = paymentPort;
        this.pricingPort = pricingPort;
        this.invoiceSellerServicePort = invoiceSellerServicePort;
        this.invoiceBuyerServicePort = invoiceBuyerServicePort;
        this.invoiceFactory = invoiceFactory;
        this.invoiceRegistration = invoiceRegistration;
    }

    public Invoice issue(final PaymentId paymentId) {
        final Optional<Invoice> issued = this.invoiceRepository.findByPayment(paymentId, InvoiceType.INVOICE);
        if (issued.isPresent()) {
            log.info("Invoice of payment {} is already issued", paymentId.value());
            return issued.get();
        }
        final Payment payment = this.paymentPort.findById(paymentId)
                .orElseThrow(() -> new InvoiceException(InvoiceException.Reason.NOT_FOUND,
                        "Payment " + paymentId.value() + " was not found"));
        final PriceQuote quote = this.pricingPort.findQuote(payment.priceQuoteId())
                .orElseThrow(() -> new InvoiceException(InvoiceException.Reason.NOT_FOUND,
                        "Price quote " + payment.priceQuoteId().value() + " was not found"));
        if (quote.gross().compareTo(payment.amount()) != 0) {
            throw new InvoiceException(InvoiceException.Reason.ILLEGAL_STATE,
                    "Gross of price quote " + payment.priceQuoteId().value() + " differs from payment "
                            + paymentId.value());
        }
        final InvoiceDraft draft = new InvoiceDraft(payment.id(), payment.priceQuoteId(), payment.method(),
                payment.paidAt(), this.invoiceSellerServicePort.currentSeller(),
                this.invoiceBuyerServicePort.findBuyer(payment.priceQuoteId(), payment.buyerTaxId()),
                quote.currency(), quote.lines().stream()
                .map(line -> new InvoiceLine(line.description(), quote.vatPercent(), line.net(), line.vat(),
                        line.gross()))
                .toList());
        final long sequence = this.invoiceNumberSequenceRepository.next(InvoiceType.INVOICE,
                this.invoiceFactory.period(payment.paidAt()));
        final Invoice invoice = this.invoiceFactory.create(draft, sequence);
        this.invoiceRegistration.register(invoice, payment.recordedBy());
        return invoice;
    }
}
