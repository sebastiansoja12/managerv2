package com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity(name = "billing.InvoiceEntity")
@Table(name = "invoices", uniqueConstraints = {
        @UniqueConstraint(name = "uq_invoices_payment_type", columnNames = {"payment_id", "type"}),
        @UniqueConstraint(name = "uq_invoices_operator_number", columnNames = {"operator_id", "number"})
})
public class InvoiceEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private InvoiceId invoiceId;

    @Column(name = "number", nullable = false, length = 32)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private InvoiceType type;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "corrected_invoice_id"))
    private InvoiceId correctedInvoiceId;

    @Column(name = "corrected_invoice_number", length = 32)
    private String correctedInvoiceNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "payment_id", nullable = false))
    private PaymentId paymentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price_quote_id", nullable = false))
    private PriceQuoteId priceQuoteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "seller_name")),
            @AttributeOverride(name = "taxId.value", column = @Column(name = "seller_tax_id", length = 32)),
            @AttributeOverride(name = "street", column = @Column(name = "seller_street")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "seller_postal_code", length = 20)),
            @AttributeOverride(name = "city", column = @Column(name = "seller_city")),
            @AttributeOverride(name = "countryCode", column = @Column(name = "seller_country_code", length = 2)),
            @AttributeOverride(name = "email", column = @Column(name = "seller_email"))
    })
    private InvoicePartyEmbeddable seller;

    @Column(name = "seller_bank_account", length = 64)
    private String sellerBankAccount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "buyer_name")),
            @AttributeOverride(name = "taxId.value", column = @Column(name = "buyer_tax_id", length = 32)),
            @AttributeOverride(name = "street", column = @Column(name = "buyer_street")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "buyer_postal_code", length = 20)),
            @AttributeOverride(name = "city", column = @Column(name = "buyer_city")),
            @AttributeOverride(name = "countryCode", column = @Column(name = "buyer_country_code", length = 2)),
            @AttributeOverride(name = "email", column = @Column(name = "buyer_email"))
    })
    private InvoicePartyEmbeddable buyer;

    @Column(name = "buyer_language", length = 8)
    private String buyerLanguage;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 3)
    private Currency currency;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "document_id")
    private UUID documentId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "invoice_lines", joinColumns = @JoinColumn(name = "invoice_id", nullable = false))
    @OrderColumn(name = "position")
    private List<InvoiceLineEmbeddable> lines = new ArrayList<>();

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public InvoiceEntity(final InvoiceId invoiceId, final String number, final InvoiceType type,
                         final InvoiceId correctedInvoiceId, final String correctedInvoiceNumber,
                         final PaymentId paymentId,
                         final PriceQuoteId priceQuoteId, final PaymentMethod paymentMethod,
                         final LocalDate issueDate, final LocalDate saleDate, final InvoicePartyEmbeddable seller,
                         final String sellerBankAccount, final InvoicePartyEmbeddable buyer,
                         final String buyerLanguage, final Currency currency, final Instant issuedAt,
                         final UUID documentId, final List<InvoiceLineEmbeddable> lines, final Long version) {
        this.invoiceId = invoiceId;
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
        this.issuedAt = issuedAt;
        this.documentId = documentId;
        this.lines = new ArrayList<>(lines);
        this.version = version;
    }
}
