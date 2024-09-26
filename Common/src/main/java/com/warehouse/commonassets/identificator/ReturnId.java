package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class ReturnId {

    private final long id;

    public ReturnId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReturnId returnId = (ReturnId) o;
        return id == returnId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
