package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.entity;

import java.util.Map;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@Converter
public class OutboxEventHeadersConverter implements AttributeConverter<Map<String, String>, String> {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();
    private static final TypeReference<Map<String, String>> HEADERS_TYPE = new TypeReference<>() {
    };

    @Override
    public String convertToDatabaseColumn(final Map<String, String> headers) {
        try {
            return JSON_MAPPER.writeValueAsString(headers);
        } catch (final JacksonException exception) {
            throw new IllegalStateException("Cannot serialize outbox event headers", exception);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(final String headers) {
        try {
            return JSON_MAPPER.readValue(headers, HEADERS_TYPE);
        } catch (final JacksonException exception) {
            throw new IllegalStateException("Cannot deserialize outbox event headers", exception);
        }
    }
}
