package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "tracking_sequence")
public class TrackingSequenceEntity {

    @Id
    private String id;

    @Version
    private Long version;

    private long nextValue;

    protected TrackingSequenceEntity() {}

    public TrackingSequenceEntity(final String id, final long start, final Long version) {
        this.id = id;
        this.nextValue = start;
        this.version = version;
    }

    public long next() {
        return nextValue++;
    }

    public String getId() {
        return id;
    }

    public long getNextValue() {
        return nextValue;
    }

    public Long getVersion() {
        return version;
    }
}
