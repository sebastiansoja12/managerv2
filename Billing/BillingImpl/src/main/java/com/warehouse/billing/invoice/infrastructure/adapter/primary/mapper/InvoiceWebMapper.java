package com.warehouse.billing.invoice.infrastructure.adapter.primary.mapper;

import com.warehouse.billing.api.dto.PriceQuoteIdDto;
import com.warehouse.billing.invoice.application.port.primary.command.FindInvoicesCommand;
import com.warehouse.billing.invoice.application.port.primary.result.InvoicePage;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.vo.InvoiceLine;
import com.warehouse.billing.invoice.domain.vo.InvoiceParty;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.api.InvoiceApiResponse;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.api.InvoiceIdDto;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.api.InvoiceLineApi;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.api.InvoicePageApiResponse;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.api.InvoicePartyApi;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.api.PaymentIdDto;
import com.warehouse.commonassets.identificator.PriceQuoteId;

public class InvoiceWebMapper {

    public FindInvoicesCommand toFindCommand(final PriceQuoteId priceQuoteId, final int page, final int size) {
        return new FindInvoicesCommand(priceQuoteId, page, size);
    }

    public InvoicePageApiResponse toResponse(final InvoicePage page) {
        return new InvoicePageApiResponse(page.invoices().stream().map(this::toResponse).toList(), page.page(),
                page.size(), page.totalElements());
    }

    public InvoiceApiResponse toResponse(final Invoice invoice) {
        return new InvoiceApiResponse(
                new InvoiceIdDto(invoice.id().value()),
                invoice.number(),
                invoice.type(),
                invoice.correctedInvoiceId() == null ? null : new InvoiceIdDto(invoice.correctedInvoiceId().value()),
                invoice.correctedInvoiceNumber(),
                new PaymentIdDto(invoice.paymentId().value()),
                new PriceQuoteIdDto(invoice.priceQuoteId().value()),
                invoice.paymentMethod(),
                invoice.issueDate(),
                invoice.saleDate(),
                toResponse(invoice.seller()),
                toResponse(invoice.buyer()),
                invoice.lines().stream().map(this::toResponse).toList(),
                invoice.net().getAmount(),
                invoice.vat().getAmount(),
                invoice.gross().getAmount(),
                invoice.currency(),
                invoice.issuedAt(),
                invoice.documentId().isPresent());
    }

    private InvoicePartyApi toResponse(final InvoiceParty party) {
        return new InvoicePartyApi(party.name(), party.taxId() == null ? null : party.taxId().value(),
                party.street(), party.postalCode(), party.city(), party.countryCode());
    }

    private InvoiceLineApi toResponse(final InvoiceLine line) {
        return new InvoiceLineApi(line.description(), line.vatPercent(), line.net().getAmount(),
                line.vat().getAmount(), line.gross().getAmount());
    }
}
