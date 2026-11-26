package com.warehouse.billing.api.dto;

import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;

public record PriceQuoteDto(PriceQuoteIdDto quoteId, BigDecimal net, BigDecimal vat, BigDecimal gross,
                            Currency currency) {
}
