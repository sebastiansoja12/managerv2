package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.math.BigDecimal;

import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class DimensionsEntity {

    @Column(name = "length", precision = 19, scale = 3)
    private BigDecimal length;

    @Column(name = "width", precision = 19, scale = 3)
    private BigDecimal width;

    @Column(name = "height", precision = 19, scale = 3)
    private BigDecimal height;

    @Enumerated(EnumType.STRING)
    @Column(name = "length_unit")
    private LengthUnit unit;

    public static DimensionsEntity from(final Dimensions dimensions) {
        if (dimensions == null) {
            return null;
        }
        final DimensionsEntity entity = new DimensionsEntity();
        entity.length = dimensions.length();
        entity.width = dimensions.width();
        entity.height = dimensions.height();
        entity.unit = dimensions.unit();
        return entity;
    }

    public Dimensions toDomain() {
        if (length == null && width == null && height == null && unit == null) {
            return null;
        }
        return new Dimensions(length, width, height, unit);
    }
}
