package com.warehouse.logisticsdelivery.domain.vo;

import java.time.LocalDateTime;

public record DeliveryTimeRange(LocalDateTime fromInclusive, LocalDateTime toExclusive) {

    public DeliveryTimeRange {
        if (!fromInclusive.isBefore(toExclusive)) {
            throw new IllegalArgumentException("Delivery time range must start before it ends");
        }
    }

    public boolean overlapsOrTouches(final DeliveryTimeRange other) {
        return !other.fromInclusive.isAfter(this.toExclusive) && !this.fromInclusive.isAfter(other.toExclusive);
    }

    public DeliveryTimeRange union(final DeliveryTimeRange other) {
        return new DeliveryTimeRange(
                this.fromInclusive.isBefore(other.fromInclusive) ? this.fromInclusive : other.fromInclusive,
                this.toExclusive.isAfter(other.toExclusive) ? this.toExclusive : other.toExclusive);
    }
}
