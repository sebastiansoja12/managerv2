package com.warehouse.message.domain.model;

import java.util.Objects;

public class MessageId {

    private final Long value;

    public MessageId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MessageId messageId = (MessageId) o;
        return Objects.equals(value, messageId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
