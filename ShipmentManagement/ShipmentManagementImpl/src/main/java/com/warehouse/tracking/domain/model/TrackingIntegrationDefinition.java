package com.warehouse.tracking.domain.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;

public record TrackingIntegrationDefinition(TrackingProviderId provider,
                                            String displayName,
                                            List<TrackingIntegrationFieldDefinition> fields) {

    public Map<String, String> resolveValues(final Map<String, String> submittedValues,
                                             final Map<String, String> storedValues) {
        final Map<String, String> resolvedValues = new LinkedHashMap<>();
        fields.forEach(field -> field.resolveValue(
                        submittedValues.get(field.key()), storedValues.get(field.key()))
                .ifPresent(value -> resolvedValues.put(field.key(), value)));
        return Map.copyOf(resolvedValues);
    }
}
