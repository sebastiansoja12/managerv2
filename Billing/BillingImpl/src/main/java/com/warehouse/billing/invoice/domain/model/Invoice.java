package com.warehouse.billing.invoice.domain.model;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.exception.InvoiceException;
import com.warehouse.billing.invoice.domain.vo.InvoiceLine;
import com.warehouse.billing.invoice.domain.vo.InvoiceParty;
import com.warehouse.billing.invoice.domain.vo.InvoiceSnapshot;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class Invoice {

    private final InvoiceId id;

    private final String number;

    private final InvoiceType type;

    private final InvoiceId correctedInvoiceId;

    private final String correctedInvoiceNumber;

    private final PaymentId paymentId;

    private final PriceQuoteId priceQuoteId;

    private final PaymentMethod paymentMethod;

    private final LocalDate issueDate;

    private final LocalDate saleDate;

    private final InvoiceParty seller;

    private final String sellerBankAccount;

    private final InvoiceParty buyer;

    private final String buyerLanguage;

    private final Currency currency;

    private final List<InvoiceLine> lines;

    private final Instant issuedAt;

    private UUID documentId;

    private final Long version;

    public Invoice(final InvoiceId id, final String number, final InvoiceType type,
                   final InvoiceId correctedInvoiceId, final String correctedInvoiceNumber,
                   final PaymentId paymentId, final PriceQuoteId priceQuoteId,
                   final PaymentMethod paymentMethod, final LocalDate issueDate, final LocalDate saleDate,
                   final InvoiceParty seller, final String sellerBankAccount, final InvoiceParty buyer,
                   final String buyerLanguage, final Currency currency, final List<InvoiceLine> lines,
                   final Instant issuedAt, final UUID documentId, final Long version) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.correctedInvoiceId = correctedInvoiceId;
        this.correctedInvoiceNumber = correctedInvoiceNumber;
        this.paymentId = paymentId;
        this.priceQuoteId = priceQuoteId;
        this.paymentMethod = paymentMethod;
        this.issueDate = issueDate;
        this.saleDate = saleDate;
        this.seller = seller;
        this.sellerBankAccount = sellerBankAccount;
        this.buyer = buyer;
        this.buyerLanguage = buyerLanguage;
        this.currency = currency;
        this.lines = List.copyOf(lines);
        this.issuedAt = issuedAt;
        this.documentId = documentId;
        this.version = version;
    }

    public void attachDocument(final UUID documentId) {
        if (documentId.equals(this.documentId)) {
            return;
        }
        if (this.documentId != null) {
            throw new InvoiceException(InvoiceException.Reason.ILLEGAL_STATE,
                    "Invoice " + this.number + " already has a document");
        }
        this.documentId = documentId;
    }

    public InvoiceSnapshot snapshot() {
        return new InvoiceSnapshot(id, number, type, correctedInvoiceId, paymentId, priceQuoteId, gross(),
                buyer.email(), buyerLanguage, documentId);
    }

    public Money net() {
        return sum(InvoiceLine::net);
    }

    public Money vat() {
        return sum(InvoiceLine::vat);
    }

    public Money gross() {
        return sum(InvoiceLine::gross);
    }

    private Money sum(final Function<InvoiceLine, Money> amount) {
        return lines.stream().map(amount).reduce(Money.from(BigDecimal.ZERO, currency), Money::add);
    }

    public InvoiceId id() {
        return id;
    }

    public String number() {
        return number;
    }

    public InvoiceType type() {
        return type;
    }

    public InvoiceId correctedInvoiceId() {
        return correctedInvoiceId;
    }

    public String correctedInvoiceNumber() {
        return correctedInvoiceNumber;
    }

    public PaymentId paymentId() {
        return paymentId;
    }

    public PriceQuoteId priceQuoteId() {
        return priceQuoteId;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public LocalDate issueDate() {
        return issueDate;
    }

    public LocalDate saleDate() {
        return saleDate;
    }

    public InvoiceParty seller() {
        return seller;
    }

    public String sellerBankAccount() {
        return sellerBankAccount;
    }

    public InvoiceParty buyer() {
        return buyer;
    }

    public String buyerLanguage() {
        return buyerLanguage;
    }

    public Currency currency() {
        return currency;
    }

    public List<InvoiceLine> lines() {
        return lines;
    }

    public Instant issuedAt() {
        return issuedAt;
    }

    public Optional<UUID> documentId() {
        return Optional.ofNullable(documentId);
    }

    public Long version() {
        return version;
    }
}
