package com.warehouse.billing.invoice.application.port.primary.command;

import com.warehouse.commonassets.identificator.PaymentId;

import java.time.Instant;

public record IssueCorrectionCommand(PaymentId paymentId, Instant refundedAt) {
}
