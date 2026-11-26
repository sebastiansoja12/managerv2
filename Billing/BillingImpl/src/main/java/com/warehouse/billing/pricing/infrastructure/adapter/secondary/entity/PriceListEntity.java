package com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.PriceListId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@Entity(name = "billing.PriceListEntity")
@Table(name = "price_lists")
public class PriceListEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private PriceListId priceListId;

    @Column(name = "template", nullable = false)
    private boolean template;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 3)
    private Currency currency;

    @Column(name = "insurance_percent", nullable = false, precision = 7, scale = 3)
    private BigDecimal insurancePercent;

    @Column(name = "vat_percent", nullable = false, precision = 7, scale = 3)
    private BigDecimal vatPercent;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "price_list_size_classes",
            joinColumns = @JoinColumn(name = "price_list_id", nullable = false))
    @OrderColumn(name = "position")
    private List<SizeClassEmbeddable> sizeClasses = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "price_list_weight_bands",
            joinColumns = @JoinColumn(name = "price_list_id", nullable = false))
    @OrderColumn(name = "position")
    private List<WeightBandEmbeddable> weightBands = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "price_list_service_levels",
            joinColumns = @JoinColumn(name = "price_list_id", nullable = false))
    @MapKeyColumn(name = "service_level", length = 20)
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "multiplier", nullable = false, precision = 7, scale = 3)
    private Map<ServiceLevel, BigDecimal> serviceLevelMultipliers = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "price_list_delivery_methods",
            joinColumns = @JoinColumn(name = "price_list_id", nullable = false))
    @MapKeyColumn(name = "delivery_method", length = 20)
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "surcharge", nullable = false, precision = 19, scale = 2)
    private Map<DeliveryOption, BigDecimal> deliveryOptionSurcharges = new HashMap<>();

    public PriceListEntity(final PriceListId priceListId,
                           final boolean template,
                           final Currency currency,
                           final BigDecimal insurancePercent,
                           final BigDecimal vatPercent,
                           final Instant updatedAt,
                           final List<SizeClassEmbeddable> sizeClasses,
                           final List<WeightBandEmbeddable> weightBands,
                           final Map<ServiceLevel, BigDecimal> serviceLevelMultipliers,
                           final Map<DeliveryOption, BigDecimal> deliveryOptionSurcharges) {
        this.priceListId = priceListId;
        this.template = template;
        this.currency = currency;
        this.insurancePercent = insurancePercent;
        this.vatPercent = vatPercent;
        this.updatedAt = updatedAt;
        this.sizeClasses = new ArrayList<>(sizeClasses);
        this.weightBands = new ArrayList<>(weightBands);
        this.serviceLevelMultipliers = new HashMap<>(serviceLevelMultipliers);
        this.deliveryOptionSurcharges = new HashMap<>(deliveryOptionSurcharges);
    }
}
