package com.warehouse.billing.invoice.application.port.primary.command;

import com.warehouse.commonassets.identificator.PaymentId;

public record IssueInvoiceCommand(PaymentId paymentId) {
}
