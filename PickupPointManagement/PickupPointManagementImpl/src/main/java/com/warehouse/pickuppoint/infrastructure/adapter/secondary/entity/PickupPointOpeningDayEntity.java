package com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity;

import com.warehouse.pickuppoint.domain.enumeration.DayAvailability;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "pickupPoint.PickupPointOpeningDayEntity")
@Table(name = "pickup_point_opening_day")
public class PickupPointOpeningDayEntity {

    @Id
    @Column(name = "opening_day_id", nullable = false)
    private UUID openingDayId;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 16)
    private DayOfWeek dayOfWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false, length = 16)
    private DayAvailability availability;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "pickup_point_opening_interval",
            joinColumns = @JoinColumn(name = "opening_day_id", nullable = false))
    @OrderColumn(name = "interval_order", nullable = false)
    private List<OpeningIntervalEmbeddable> intervals = new ArrayList<>();

    protected PickupPointOpeningDayEntity() {
    }

    public PickupPointOpeningDayEntity(
            final UUID openingDayId,
            final DayOfWeek dayOfWeek,
            final DayAvailability availability,
            final List<OpeningIntervalEmbeddable> intervals) {
        this.openingDayId = openingDayId;
        this.dayOfWeek = dayOfWeek;
        this.availability = availability;
        this.intervals.addAll(intervals);
    }

    public UUID getOpeningDayId() {
        return this.openingDayId;
    }

    public DayOfWeek getDayOfWeek() {
        return this.dayOfWeek;
    }

    public DayAvailability getAvailability() {
        return this.availability;
    }

    public List<OpeningIntervalEmbeddable> getIntervals() {
        return List.copyOf(this.intervals);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PickupPointOpeningDayEntity that)) {
            return false;
        }
        return Objects.equals(this.openingDayId, that.openingDayId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.openingDayId);
    }
}
