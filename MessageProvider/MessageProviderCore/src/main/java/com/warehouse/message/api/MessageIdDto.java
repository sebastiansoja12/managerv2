package com.warehouse.message.api;

import java.util.Objects;

import com.warehouse.message.domain.model.Message;

public record MessageIdDto(Long value) {

    public static MessageIdDto from(final Message message) {
        return new MessageIdDto(message.getMessageId().getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MessageIdDto that = (MessageIdDto) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
