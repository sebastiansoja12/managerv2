package com.warehouse.billing.invoice.application.service;

import com.warehouse.billing.invoice.application.port.primary.command.IssueCorrectionCommand;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceNumberSequenceRepository;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.service.InvoiceFactory;
import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.domain.model.Payment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CorrectionInvoiceIssuer {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceNumberSequenceRepository invoiceNumberSequenceRepository;

    private final PaymentPort paymentPort;

    private final InvoiceFactory invoiceFactory;

    private final InvoiceRegistration invoiceRegistration;

    private final InvoiceIssuer invoiceIssuer;

    public CorrectionInvoiceIssuer(final InvoiceRepository invoiceRepository,
                                   final InvoiceNumberSequenceRepository invoiceNumberSequenceRepository,
                                   final PaymentPort paymentPort,
                                   final InvoiceFactory invoiceFactory,
                                   final InvoiceRegistration invoiceRegistration,
                                   final InvoiceIssuer invoiceIssuer) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceNumberSequenceRepository = invoiceNumberSequenceRepository;
        this.paymentPort = paymentPort;
        this.invoiceFactory = invoiceFactory;
        this.invoiceRegistration = invoiceRegistration;
        this.invoiceIssuer = invoiceIssuer;
    }

    public void issue(final IssueCorrectionCommand command) {
        if (this.invoiceRepository.findByPayment(command.paymentId(), InvoiceType.CORRECTION).isPresent()) {
            log.info("Correction invoice of payment {} is already issued", command.paymentId().value());
            return;
        }
        final Invoice original = this.invoiceIssuer.issue(command.paymentId());
        final long sequence = this.invoiceNumberSequenceRepository.next(InvoiceType.CORRECTION,
                this.invoiceFactory.period(command.refundedAt()));
        this.invoiceRegistration.register(
                this.invoiceFactory.createCorrection(original, command.refundedAt(), sequence),
                this.paymentPort.findById(command.paymentId()).map(Payment::recordedBy).orElse(null));
    }
}
