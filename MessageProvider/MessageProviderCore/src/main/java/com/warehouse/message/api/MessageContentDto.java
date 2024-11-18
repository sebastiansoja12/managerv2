package com.warehouse.message.api;

import com.warehouse.message.domain.model.Message;

import java.util.Objects;

public final class MessageContentDto {

    private String value;

    public MessageContentDto() {

    }

    public MessageContentDto(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MessageContentDto from(final Message message) {
        return new MessageContentDto(message.getMessageContent());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MessageContentDto that = (MessageContentDto) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
