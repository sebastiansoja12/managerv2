package com.warehouse.deliverymissed.domain.vo;

import java.util.Objects;

public record IncidentReport(String value) {

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final IncidentReport that = (IncidentReport) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
