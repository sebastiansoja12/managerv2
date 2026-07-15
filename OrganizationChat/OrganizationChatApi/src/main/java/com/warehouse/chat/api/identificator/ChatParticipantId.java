package com.warehouse.chat.api.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChatParticipantId implements Serializable {

    private Long value;

    protected ChatParticipantId() {
    }

    public ChatParticipantId(final Long value) {
        this.value = value;
    }

    public static ChatParticipantId of(final Long value) {
        return new ChatParticipantId(value);
    }

    public static ChatParticipantId generate() {
        return new ChatParticipantId(ChatIdentifierValue.generate());
    }

    public Long getValue() {
        return value;
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ChatParticipantId that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
