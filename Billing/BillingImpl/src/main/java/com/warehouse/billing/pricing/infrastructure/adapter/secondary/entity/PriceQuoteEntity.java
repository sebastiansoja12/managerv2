package com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.PriceListId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "billing.PriceQuoteEntity")
@Table(name = "price_quotes")
public class PriceQuoteEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private PriceQuoteId priceQuoteId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price_list_id", nullable = false))
    private PriceListId priceListId;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 3)
    private Currency currency;

    @Column(name = "vat_percent", nullable = false, precision = 7, scale = 3)
    private BigDecimal vatPercent;

    @Column(name = "quoted_at", nullable = false)
    private Instant quotedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "price_quote_lines", joinColumns = @JoinColumn(name = "quote_id", nullable = false))
    @OrderColumn(name = "position")
    private List<PriceQuoteLineEmbeddable> lines = new ArrayList<>();

    public PriceQuoteEntity(final PriceQuoteId priceQuoteId,
                            final PriceListId priceListId,
                            final Currency currency,
                            final BigDecimal vatPercent,
                            final Instant quotedAt,
                            final List<PriceQuoteLineEmbeddable> lines) {
        this.priceQuoteId = priceQuoteId;
        this.priceListId = priceListId;
        this.currency = currency;
        this.vatPercent = vatPercent;
        this.quotedAt = quotedAt;
        this.lines = new ArrayList<>(lines);
    }
}
