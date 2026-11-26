package com.warehouse.billing.pricing.domain.vo;

import com.warehouse.billing.pricing.domain.enumeration.PriceQuoteLineType;
import com.warehouse.commonassets.model.Money;

public record PriceQuoteLine(PriceQuoteLineType type, String description, Money net, Money vat, Money gross) {
}
