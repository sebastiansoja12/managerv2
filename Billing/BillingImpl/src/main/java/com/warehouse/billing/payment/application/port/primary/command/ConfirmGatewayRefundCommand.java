package com.warehouse.billing.payment.application.port.primary.command;

import com.warehouse.commonassets.identificator.WebhookEventId;

public record ConfirmGatewayRefundCommand(String gatewayPaymentIntent, WebhookEventId webhookEventId) {
}
