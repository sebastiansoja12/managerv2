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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "pickupPoint.PickupPointScheduleExceptionEntity")
@Table(name = "pickup_point_schedule_exception")
public class PickupPointScheduleExceptionEntity {

    @Id
    @Column(name = "schedule_exception_id", nullable = false)
    private UUID scheduleExceptionId;

    @Column(name = "exception_date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false, length = 16)
    private DayAvailability availability;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "pickup_point_exception_interval",
            joinColumns = @JoinColumn(name = "schedule_exception_id", nullable = false))
    @OrderColumn(name = "interval_order", nullable = false)
    private List<OpeningIntervalEmbeddable> intervals = new ArrayList<>();

    protected PickupPointScheduleExceptionEntity() {
    }

    public PickupPointScheduleExceptionEntity(
            final UUID scheduleExceptionId,
            final LocalDate date,
            final DayAvailability availability,
            final List<OpeningIntervalEmbeddable> intervals) {
        this.scheduleExceptionId = scheduleExceptionId;
        this.date = date;
        this.availability = availability;
        this.intervals.addAll(intervals);
    }

    public UUID getScheduleExceptionId() {
        return this.scheduleExceptionId;
    }

    public LocalDate getDate() {
        return this.date;
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
        if (!(object instanceof PickupPointScheduleExceptionEntity that)) {
            return false;
        }
        return Objects.equals(this.scheduleExceptionId, that.scheduleExceptionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.scheduleExceptionId);
    }
}
