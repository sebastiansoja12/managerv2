package com.warehouse.billing.pricing.domain.model;

import com.warehouse.billing.pricing.domain.vo.PriceQuoteLine;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.PriceListId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

public class PriceQuote {

    private final PriceQuoteId id;

    private final PriceListId priceListId;

    private final Currency currency;

    private final List<PriceQuoteLine> lines;

    private final BigDecimal vatPercent;

    private final Instant quotedAt;

    public PriceQuote(final PriceQuoteId id, final PriceListId priceListId, final Currency currency,
                      final List<PriceQuoteLine> lines, final BigDecimal vatPercent, final Instant quotedAt) {
        this.id = id;
        this.priceListId = priceListId;
        this.currency = currency;
        this.lines = List.copyOf(lines);
        this.vatPercent = vatPercent;
        this.quotedAt = quotedAt;
    }

    public PriceQuoteId id() {
        return id;
    }

    public PriceListId priceListId() {
        return priceListId;
    }

    public Currency currency() {
        return currency;
    }

    public List<PriceQuoteLine> lines() {
        return lines;
    }

    public BigDecimal vatPercent() {
        return vatPercent;
    }

    public Instant quotedAt() {
        return quotedAt;
    }

    public Money net() {
        return sum(PriceQuoteLine::net);
    }

    public Money vat() {
        return sum(PriceQuoteLine::vat);
    }

    public Money gross() {
        return sum(PriceQuoteLine::gross);
    }

    private Money sum(final Function<PriceQuoteLine, Money> amount) {
        return lines.stream().map(amount).reduce(Money.from(BigDecimal.ZERO, currency), Money::add);
    }
}
