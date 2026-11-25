package com.warehouse.billing.invoice.infrastructure.adapter.secondary.mapper;

import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.vo.InvoiceLine;
import com.warehouse.billing.invoice.domain.vo.InvoiceParty;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceEntity;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceLineEmbeddable;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoicePartyEmbeddable;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.model.Money;

public class InvoicePersistenceMapper {

    public InvoiceEntity toEntity(final Invoice invoice) {
        return new InvoiceEntity(invoice.id(), invoice.number(), invoice.type(), invoice.correctedInvoiceId(),
                invoice.correctedInvoiceNumber(), invoice.paymentId(), invoice.priceQuoteId(),
                invoice.paymentMethod(), invoice.issueDate(), invoice.saleDate(), toEmbeddable(invoice.seller()),
                invoice.sellerBankAccount(), toEmbeddable(invoice.buyer()), invoice.buyerLanguage(),
                invoice.currency(), invoice.issuedAt(), invoice.documentId().orElse(null),
                invoice.lines().stream().map(this::toEmbeddable).toList(), invoice.version());
    }

    public Invoice toDomain(final InvoiceEntity entity) {
        return new Invoice(entity.getInvoiceId(), entity.getNumber(), entity.getType(),
                entity.getCorrectedInvoiceId(), entity.getCorrectedInvoiceNumber(), entity.getPaymentId(),
                entity.getPriceQuoteId(), entity.getPaymentMethod(), entity.getIssueDate(), entity.getSaleDate(),
                toDomain(entity.getSeller()), entity.getSellerBankAccount(), toDomain(entity.getBuyer()),
                entity.getBuyerLanguage(), entity.getCurrency(),
                entity.getLines().stream().map(line -> toDomain(line, entity.getCurrency())).toList(),
                entity.getIssuedAt(), entity.getDocumentId(), entity.getVersion());
    }

    private InvoicePartyEmbeddable toEmbeddable(final InvoiceParty party) {
        return new InvoicePartyEmbeddable(party.name(), party.taxId(), party.street(), party.postalCode(),
                party.city(), party.countryCode(), party.email());
    }

    private InvoiceParty toDomain(final InvoicePartyEmbeddable party) {
        if (party == null) {
            return InvoiceParty.unknown(null);
        }
        return new InvoiceParty(party.getName(), party.getTaxId(), party.getStreet(), party.getPostalCode(),
                party.getCity(), party.getCountryCode(), party.getEmail());
    }

    private InvoiceLineEmbeddable toEmbeddable(final InvoiceLine line) {
        return new InvoiceLineEmbeddable(line.description(), line.vatPercent(), line.net().getAmount(),
                line.vat().getAmount(), line.gross().getAmount());
    }

    private InvoiceLine toDomain(final InvoiceLineEmbeddable line, final Currency currency) {
        return new InvoiceLine(line.getDescription(), line.getVatPercent(), Money.from(line.getNet(), currency),
                Money.from(line.getVat(), currency), Money.from(line.getGross(), currency));
    }
}
