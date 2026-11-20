package com.warehouse.eventoutbox.consumption;

import java.lang.reflect.Type;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.converter.StringJacksonJsonMessageConverter;
import org.springframework.util.TypeUtils;

import com.warehouse.eventoutbox.integration.registry.IntegrationEventDefinition;

import tools.jackson.databind.json.JsonMapper;

public class IntegrationEventMessageConverter extends StringJacksonJsonMessageConverter {

    private final IntegrationEventTypeResolver typeResolver;

    public IntegrationEventMessageConverter(final JsonMapper jsonMapper,
                                            final IntegrationEventTypeResolver typeResolver) {
        super(jsonMapper);
        this.typeResolver = typeResolver;
    }

    @Override
    protected Object extractAndConvertValue(final ConsumerRecord<?, ?> record, final Type type) {
        return super.extractAndConvertValue(record, targetType(record, type));
    }

    private Type targetType(final ConsumerRecord<?, ?> record, final Type parameterType) {
        return this.typeResolver.resolve(record.headers())
                .<Class<?>>map(IntegrationEventDefinition::eventClass)
                .filter(eventClass -> parameterType == null || TypeUtils.isAssignable(parameterType, eventClass))
                .<Type>map(eventClass -> eventClass)
                .orElse(parameterType);
    }
}
