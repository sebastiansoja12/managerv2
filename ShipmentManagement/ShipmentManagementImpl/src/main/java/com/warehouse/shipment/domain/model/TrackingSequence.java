package com.warehouse.shipment.domain.model;

public class TrackingSequence {

    private final String id;
    private long nextValue;
    private long version;

    public TrackingSequence(final String id, final long nextValue) {
        this.id = id;
        this.nextValue = nextValue;
        this.version = 0L;
    }

    public String getId() {
        return id;
    }

    public long getNextValue() {
        return nextValue;
    }

    public long getVersion() {
        return version;
    }

    public void setNextValue(final long nextValue) {
        this.nextValue = nextValue;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public synchronized long next() {
        final long current = nextValue;
        nextValue++;
        version++;
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

