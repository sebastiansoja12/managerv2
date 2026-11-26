package com.warehouse.billing.payment.application.port.primary.command;

import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.WebhookEventId;

public record ConfirmOnlinePaymentCommand(PaymentId paymentId, String gatewayReference, String gatewayPaymentIntent,
                                          WebhookEventId webhookEventId) {
}
