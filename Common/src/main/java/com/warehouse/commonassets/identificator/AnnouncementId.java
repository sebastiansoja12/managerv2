package com.warehouse.commonassets.identificator;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class AnnouncementId {

    private Long value;

    protected AnnouncementId() {
    }

    public AnnouncementId(final Long value) {
        this.value = value;
    }

    public static AnnouncementId of(final Long value) {
        return new AnnouncementId(value);
    }

    @JsonValue
    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AnnouncementId that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
