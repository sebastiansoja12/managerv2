package com.warehouse.message.api;

import com.warehouse.message.domain.model.Message;

public record TitleDto(String value) {

    public static TitleDto from(final Message message) {
        return new TitleDto(message.getTitle());
    }
}
