package com.warehouse.billing.pricing.domain.model;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.billing.pricing.domain.exception.PriceListException;
import com.warehouse.billing.pricing.domain.vo.SizeClass;
import com.warehouse.billing.pricing.domain.vo.WeightBand;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceListId;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class PriceList {

    private final PriceListId id;

    private final OperatorId operatorId;

    private Currency currency;

    private List<SizeClass> sizeClasses;

    private List<WeightBand> weightBands;

    private Map<ServiceLevel, BigDecimal> serviceLevelMultipliers;

    private Map<DeliveryOption, BigDecimal> deliveryOptionSurcharges;

    private BigDecimal insurancePercent;

    private BigDecimal vatPercent;

    private Instant updatedAt;

    public PriceList(final PriceListId id, final OperatorId operatorId, final Currency currency,
                     final List<SizeClass> sizeClasses, final List<WeightBand> weightBands,
                     final Map<ServiceLevel, BigDecimal> serviceLevelMultipliers,
                     final Map<DeliveryOption, BigDecimal> deliveryOptionSurcharges,
                     final BigDecimal insurancePercent, final BigDecimal vatPercent, final Instant updatedAt) {
        if (!serviceLevelMultipliers.keySet().containsAll(EnumSet.allOf(ServiceLevel.class))) {
            throw new PriceListException(PriceListException.Reason.INVALID_REQUEST,
                    "Price list must define a multiplier for every service level");
        }
        if (!deliveryOptionSurcharges.keySet().containsAll(EnumSet.allOf(DeliveryOption.class))) {
            throw new PriceListException(PriceListException.Reason.INVALID_REQUEST,
                    "Price list must define a surcharge for every delivery option");
        }
        this.id = id;
        this.operatorId = operatorId;
        this.currency = currency;
        this.sizeClasses = List.copyOf(sizeClasses);
        this.weightBands = List.copyOf(weightBands);
        this.serviceLevelMultipliers = Map.copyOf(serviceLevelMultipliers);
        this.deliveryOptionSurcharges = Map.copyOf(deliveryOptionSurcharges);
        this.insurancePercent = insurancePercent;
        this.vatPercent = vatPercent;
        this.updatedAt = updatedAt;
    }

    public void replace(final PriceList replacement) {
        this.currency = replacement.currency;
        this.sizeClasses = replacement.sizeClasses;
        this.weightBands = replacement.weightBands;
        this.serviceLevelMultipliers = replacement.serviceLevelMultipliers;
        this.deliveryOptionSurcharges = replacement.deliveryOptionSurcharges;
        this.insurancePercent = replacement.insurancePercent;
        this.vatPercent = replacement.vatPercent;
        this.updatedAt = replacement.updatedAt;
    }

    public PriceListId id() {
        return id;
    }

    public OperatorId operatorId() {
        return operatorId;
    }

    public Currency currency() {
        return currency;
    }

    public List<SizeClass> sizeClasses() {
        return sizeClasses;
    }

    public List<WeightBand> weightBands() {
        return weightBands;
    }

    public Map<ServiceLevel, BigDecimal> serviceLevelMultipliers() {
        return serviceLevelMultipliers;
    }

    public Map<DeliveryOption, BigDecimal> deliveryOptionSurcharges() {
        return deliveryOptionSurcharges;
    }

    public BigDecimal insurancePercent() {
        return insurancePercent;
    }

    public BigDecimal vatPercent() {
        return vatPercent;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
