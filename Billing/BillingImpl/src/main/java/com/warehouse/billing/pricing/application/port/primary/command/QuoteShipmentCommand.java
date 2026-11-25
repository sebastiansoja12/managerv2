package com.warehouse.billing.pricing.application.port.primary.command;

import com.warehouse.billing.pricing.domain.vo.PricingInput;

public record QuoteShipmentCommand(PricingInput input) {
}
