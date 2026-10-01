package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class DeliveryStepId implements Serializable {

    private UUID value;

    protected DeliveryStepId() {
    }

    public DeliveryStepId(final UUID value) {
        this.value = value;
    }

    public static DeliveryStepId generate() {
        return new DeliveryStepId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DeliveryStepId that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
