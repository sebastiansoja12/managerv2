package com.warehouse.shipment.domain.model;

public class TrackingSequence {

    private final String id;
    private long nextValue;
    private Long version;

    public TrackingSequence(final String id, final long nextValue) {
        this(id, nextValue, null);
    }

    public TrackingSequence(final String id, final long nextValue, final Long version) {
        this.id = id;
        this.nextValue = nextValue;
        this.version = version;
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

    public void setNextValue(final long nextValue) {
        this.nextValue = nextValue;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public synchronized long next() {
        final long current = nextValue;
        nextValue++;
        return current;
    }

    @Override
    public String toString() {
        return "TrackingSequence{" +
                "id='" + id + '\'' +
                ", nextValue=" + nextValue +
                ", version=" + version +
                '}';
    }
}
