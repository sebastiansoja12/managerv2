package com.warehouse.logisticsdelivery.domain.model;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.logisticsdelivery.domain.vo.Courier;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CourierRoster {

    private final List<Courier> couriers;
    private final Map<SupplierId, Long> activeDeliveries;

    public CourierRoster(final List<Courier> couriers, final Map<SupplierId, Long> activeDeliveries) {
        this.couriers = List.copyOf(couriers);
        this.activeDeliveries = new HashMap<>(activeDeliveries);
    }

    public Optional<Courier> select(final String postalCode) {
        return this.couriers.stream()
                .filter(courier -> courier.covers(postalCode))
                .filter(courier -> courier.canTake(load(courier)))
                .min(Comparator.comparingLong(this::load)
                        .thenComparing(courier -> courier.supplierId().value()));
    }

    public void registerAssignment(final Courier courier) {
        this.activeDeliveries.merge(courier.supplierId(), 1L, Long::sum);
    }

    public boolean isEmpty() {
        return this.couriers.isEmpty();
    }

    public CourierRoster copy() {
        return new CourierRoster(this.couriers, this.activeDeliveries);
    }

    private long load(final Courier courier) {
        return this.activeDeliveries.getOrDefault(courier.supplierId(), 0L);
    }
}
