package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.billing.api.event.InvoiceIssuedIntegrationEvent;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.InvoiceMailRequestMapper;

public class InvoiceMailKafkaListener {

    private final MailPort mailPort;
    private final InvoiceMailRequestMapper invoiceMailRequestMapper;

    public InvoiceMailKafkaListener(final MailPort mailPort,
                                    final InvoiceMailRequestMapper invoiceMailRequestMapper) {
        this.mailPort = mailPort;
        this.invoiceMailRequestMapper = invoiceMailRequestMapper;
    }

    @IntegrationEventListener(group = "manager-mail-invoice-issued")
    public void handle(final InvoiceIssuedIntegrationEvent event) {
        this.mailPort.send(this.invoiceMailRequestMapper.mapIssued(event));
    }
}
