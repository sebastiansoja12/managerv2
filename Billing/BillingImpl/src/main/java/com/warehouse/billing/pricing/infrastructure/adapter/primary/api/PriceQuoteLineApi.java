package com.warehouse.billing.pricing.infrastructure.adapter.primary.api;

import com.warehouse.billing.pricing.domain.enumeration.PriceQuoteLineType;

import java.math.BigDecimal;

public record PriceQuoteLineApi(PriceQuoteLineType type, String description, BigDecimal net, BigDecimal vat,
                                BigDecimal gross) {
}
