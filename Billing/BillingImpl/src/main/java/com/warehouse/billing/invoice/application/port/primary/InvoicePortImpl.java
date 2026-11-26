package com.warehouse.billing.invoice.application.port.primary;

import com.warehouse.billing.invoice.application.port.primary.command.FindInvoicesCommand;
import com.warehouse.billing.invoice.application.port.primary.command.IssueCorrectionCommand;
import com.warehouse.billing.invoice.application.port.primary.command.IssueInvoiceCommand;
import com.warehouse.billing.invoice.application.port.primary.result.InvoicePage;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceDocumentServicePort;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.application.service.CorrectionInvoiceIssuer;
import com.warehouse.billing.invoice.application.service.InvoiceIssuer;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.vo.InvoiceDocument;
import com.warehouse.commonassets.identificator.InvoiceId;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class InvoicePortImpl implements InvoicePort {

    private final InvoiceIssuer invoiceIssuer;

    private final CorrectionInvoiceIssuer correctionInvoiceIssuer;

    private final InvoiceRepository invoiceRepository;

    private final InvoiceDocumentServicePort invoiceDocumentServicePort;

    public InvoicePortImpl(final InvoiceIssuer invoiceIssuer,
                           final CorrectionInvoiceIssuer correctionInvoiceIssuer,
                           final InvoiceRepository invoiceRepository,
                           final InvoiceDocumentServicePort invoiceDocumentServicePort) {
        this.invoiceIssuer = invoiceIssuer;
        this.correctionInvoiceIssuer = correctionInvoiceIssuer;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDocumentServicePort = invoiceDocumentServicePort;
    }

    @Override
    @Transactional
    public void issueForPayment(final IssueInvoiceCommand command) {
        this.invoiceIssuer.issue(command.paymentId());
    }

    @Override
    @Transactional
    public void issueCorrection(final IssueCorrectionCommand command) {
        this.correctionInvoiceIssuer.issue(command);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoicePage find(final FindInvoicesCommand command) {
        return this.invoiceRepository.find(command);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceDocument> findDocument(final InvoiceId invoiceId) {
        return this.invoiceRepository.findById(invoiceId)
                .flatMap(Invoice::documentId)
                .flatMap(this.invoiceDocumentServicePort::load);
    }
}
