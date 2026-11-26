package com.warehouse.billing.invoice.application.service;

import com.warehouse.billing.invoice.application.port.secondary.InvoiceDocumentServicePort;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.domain.event.InvoiceIssued;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;

import java.time.Clock;

public class InvoiceRegistration {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceDocumentServicePort invoiceDocumentServicePort;

    private final DomainEventPublisher domainEventPublisher;

    private final Clock clock;

    public InvoiceRegistration(final InvoiceRepository invoiceRepository,
                               final InvoiceDocumentServicePort invoiceDocumentServicePort,
                               final DomainEventPublisher domainEventPublisher,
                               final Clock clock) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDocumentServicePort = invoiceDocumentServicePort;
        this.domainEventPublisher = domainEventPublisher;
        this.clock = clock;
    }

    public void register(final Invoice invoice, final UserId uploadedBy) {
        invoice.attachDocument(this.invoiceDocumentServicePort.store(invoice, uploadedBy));
        this.invoiceRepository.save(invoice);
        this.domainEventPublisher.publish(new InvoiceIssued(invoice.snapshot(), this.clock.instant()));
    }
}
