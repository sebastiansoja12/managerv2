package com.warehouse.shipment.domain.vo;

import java.util.Objects;

public class VoronoiResponse {

    private final String value;

    public VoronoiResponse(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VoronoiResponse voronoiResponse = (VoronoiResponse) o;
        return Objects.equals(value, voronoiResponse.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
