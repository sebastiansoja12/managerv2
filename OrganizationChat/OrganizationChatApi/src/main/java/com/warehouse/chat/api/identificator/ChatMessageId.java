package com.warehouse.chat.api.identificator;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChatMessageId implements Serializable {

    private Long value;

    protected ChatMessageId() {
    }

    public ChatMessageId(final Long value) {
        this.value = value;
    }

    public static ChatMessageId of(final Long value) {
        return new ChatMessageId(value);
    }

    public static ChatMessageId generate() {
        return new ChatMessageId(ChatIdentifierValue.generate());
    }

    public Long getValue() {
        return value;
    }

    @JsonValue
    public String serializedValue() {
        return String.valueOf(value);
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ChatMessageId that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
