package com.warehouse.billing.invoice.infrastructure.adapter.primary.mapper;

import com.warehouse.billing.api.event.PaymentRefundedIntegrationEvent;
import com.warehouse.billing.api.event.PriceQuotePaidIntegrationEvent;
import com.warehouse.billing.invoice.application.port.primary.command.IssueCorrectionCommand;
import com.warehouse.billing.invoice.application.port.primary.command.IssueInvoiceCommand;

public class InvoiceIntegrationEventMapper {

    public IssueInvoiceCommand toCommand(final PriceQuotePaidIntegrationEvent event) {
        return new IssueInvoiceCommand(event.payload().paymentId());
    }

    public IssueCorrectionCommand toCommand(final PaymentRefundedIntegrationEvent event) {
        return new IssueCorrectionCommand(event.paymentId(), event.refundedAt());
    }
}
