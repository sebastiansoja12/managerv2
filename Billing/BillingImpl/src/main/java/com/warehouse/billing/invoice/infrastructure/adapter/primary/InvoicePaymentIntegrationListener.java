package com.warehouse.billing.invoice.infrastructure.adapter.primary;

import com.warehouse.billing.api.event.PaymentRefundedIntegrationEvent;
import com.warehouse.billing.api.event.PriceQuotePaidIntegrationEvent;
import com.warehouse.billing.invoice.application.port.primary.InvoicePort;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.mapper.InvoiceIntegrationEventMapper;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import org.springframework.stereotype.Component;

@Component
public class InvoicePaymentIntegrationListener {

    private final InvoicePort invoicePort;

    private final InvoiceIntegrationEventMapper invoiceIntegrationEventMapper;

    public InvoicePaymentIntegrationListener(final InvoicePort invoicePort,
                                             final InvoiceIntegrationEventMapper invoiceIntegrationEventMapper) {
        this.invoicePort = invoicePort;
        this.invoiceIntegrationEventMapper = invoiceIntegrationEventMapper;
    }

    @IntegrationEventListener(group = "manager-billing-invoice")
    public void handle(final PriceQuotePaidIntegrationEvent event) {
        this.invoicePort.issueForPayment(this.invoiceIntegrationEventMapper.toCommand(event));
    }

    @IntegrationEventListener(group = "manager-billing-invoice")
    public void handle(final PaymentRefundedIntegrationEvent event) {
        this.invoicePort.issueCorrection(this.invoiceIntegrationEventMapper.toCommand(event));
    }
}
