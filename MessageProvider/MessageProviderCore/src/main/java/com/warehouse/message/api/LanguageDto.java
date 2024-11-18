package com.warehouse.message.api;

import com.warehouse.message.domain.model.Message;

public record LanguageDto(String value) {

    public static LanguageDto from(final Message message) {
        return new LanguageDto(message.getLanguage());
    }
}
