package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.math.BigDecimal;

import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class WeightEntity {

    @Column(name = "weight_value", precision = 19, scale = 3)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_unit")
    private WeightUnit unit;

    public static WeightEntity from(final Weight weight) {
        if (weight == null) {
            return null;
        }
        final WeightEntity entity = new WeightEntity();
        entity.value = weight.value();
        entity.unit = weight.unit();
        return entity;
    }

    public Weight toDomain() {
        if (value == null && unit == null) {
            return null;
        }
        return new Weight(value, unit);
    }
}
