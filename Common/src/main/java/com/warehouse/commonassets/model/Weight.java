package com.warehouse.commonassets.model;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.WeightUnit;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Weight {
    private BigDecimal weight;
    @Enumerated(EnumType.STRING)
    private WeightUnit unit;

    public Weight() {
    }

    public Weight(final BigDecimal weight, final WeightUnit unit) {
        this.unit = unit;
        this.weight = weight;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    public void setUnit(final WeightUnit unit) {
        this.unit = unit;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(final BigDecimal weight) {
        this.weight = weight;
    }
}
