package com.warehouse.billing.invoice.application.port.primary;

import com.warehouse.billing.invoice.application.port.primary.command.FindInvoicesCommand;
import com.warehouse.billing.invoice.application.port.primary.result.InvoicePage;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.identificator.PaymentId;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryInvoiceRepository implements InvoiceRepository {

    private final Map<InvoiceId, Invoice> invoices = new LinkedHashMap<>();

    @Override
    public void save(final Invoice invoice) {
        this.invoices.put(invoice.id(), invoice);
    }

    @Override
    public Optional<Invoice> findById(final InvoiceId invoiceId) {
        return Optional.ofNullable(this.invoices.get(invoiceId));
    }

    @Override
    public Optional<Invoice> findByPayment(final PaymentId paymentId, final InvoiceType type) {
        return this.invoices.values().stream()
                .filter(invoice -> invoice.paymentId().equals(paymentId) && invoice.type() == type)
                .findFirst();
    }

    @Override
    public InvoicePage find(final FindInvoicesCommand command) {
        final List<Invoice> all = List.copyOf(this.invoices.values());
        return new InvoicePage(all, command.page(), command.size(), all.size());
    }

    List<Invoice> all() {
        return List.copyOf(this.invoices.values());
    }
}
