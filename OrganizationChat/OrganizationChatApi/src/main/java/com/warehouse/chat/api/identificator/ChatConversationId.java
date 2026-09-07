package com.warehouse.chat.api.identificator;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChatConversationId implements Serializable {

    private Long value;

    protected ChatConversationId() {
    }

    public ChatConversationId(final Long value) {
        this.value = value;
    }

    public static ChatConversationId of(final Long value) {
        return new ChatConversationId(value);
    }

    public static ChatConversationId generate() {
        return new ChatConversationId(ChatIdentifierValue.generate());
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
        if (!(object instanceof ChatConversationId that)) {
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
