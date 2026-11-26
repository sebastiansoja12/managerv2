package com.warehouse.billing.invoice.application.port.primary;

import com.warehouse.billing.invoice.application.port.primary.command.FindInvoicesCommand;
import com.warehouse.billing.invoice.application.port.primary.command.IssueCorrectionCommand;
import com.warehouse.billing.invoice.application.port.primary.command.IssueInvoiceCommand;
import com.warehouse.billing.invoice.application.port.primary.result.InvoicePage;
import com.warehouse.billing.invoice.domain.vo.InvoiceDocument;
import com.warehouse.commonassets.identificator.InvoiceId;

import java.util.Optional;

public interface InvoicePort {

    void issueForPayment(final IssueInvoiceCommand command);

    void issueCorrection(final IssueCorrectionCommand command);

    InvoicePage find(final FindInvoicesCommand command);

    Optional<InvoiceDocument> findDocument(final InvoiceId invoiceId);
}
