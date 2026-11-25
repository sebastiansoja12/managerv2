package com.warehouse.billing.invoice.application.port.secondary;

import com.warehouse.billing.invoice.application.port.primary.command.FindInvoicesCommand;
import com.warehouse.billing.invoice.application.port.primary.result.InvoicePage;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.identificator.PaymentId;

import java.util.Optional;

public interface InvoiceRepository {

    void save(final Invoice invoice);

    Optional<Invoice> findById(final InvoiceId invoiceId);

    Optional<Invoice> findByPayment(final PaymentId paymentId, final InvoiceType type);

    InvoicePage find(final FindInvoicesCommand command);
}
