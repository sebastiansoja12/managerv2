package com.warehouse.billing.pricing.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;
import java.util.List;

public record PriceQuoteApiResponse(List<PriceQuoteLineApi> lines, BigDecimal net, BigDecimal vat,
                                    BigDecimal gross, BigDecimal vatPercent, Currency currency) {
}
