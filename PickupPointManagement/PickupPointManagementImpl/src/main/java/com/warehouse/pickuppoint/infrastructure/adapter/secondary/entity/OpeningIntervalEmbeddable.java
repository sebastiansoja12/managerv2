package com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OpeningIntervalEmbeddable {

    @Column(name = "start_minute", nullable = false)
    private int startMinute;

    @Column(name = "end_minute", nullable = false)
    private int endMinute;

    protected OpeningIntervalEmbeddable() {
    }

    public OpeningIntervalEmbeddable(final int startMinute, final int endMinute) {
        this.startMinute = startMinute;
        this.endMinute = endMinute;
    }

    public int getStartMinute() {
        return this.startMinute;
    }

    public int getEndMinute() {
        return this.endMinute;
    }
}
