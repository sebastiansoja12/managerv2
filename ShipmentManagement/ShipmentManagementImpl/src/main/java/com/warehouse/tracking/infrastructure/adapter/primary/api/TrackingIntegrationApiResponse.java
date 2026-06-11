package com.warehouse.tracking.infrastructure.adapter.primary.api;

import java.util.Map;
import java.util.Set;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.model.TrackingIntegrationDefinition;
import com.warehouse.tracking.domain.model.TrackingIntegrationFieldDefinition;

public record TrackingIntegrationApiResponse(TrackingProviderId provider,
                                             String displayName,
                                             boolean configured,
                                             boolean enabled,
                                             Map<String, String> values,
                                             Set<String> configuredSecretFields) {

    public static TrackingIntegrationApiResponse from(final TrackingIntegrationDefinition definition,
                                                      final TrackingIntegrationConfiguration configuration) {
        if (configuration == null) {
            return new TrackingIntegrationApiResponse(
                    definition.provider(), definition.displayName(), false, false, Map.of(), Set.of());
        }
        final Set<String> secretFields = definition.fields().stream()
                .filter(TrackingIntegrationFieldDefinition::secret)
                .filter(field -> configuration.hasValue(field.key()))
                .map(TrackingIntegrationFieldDefinition::key)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
        final Map<String, String> visibleValues = definition.fields().stream()
                .filter(field -> !field.secret())
                .filter(field -> configuration.hasValue(field.key()))
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        TrackingIntegrationFieldDefinition::key,
                        field -> configuration.getValue(field.key())));
        return new TrackingIntegrationApiResponse(
                definition.provider(), definition.displayName(), true, configuration.isEnabled(),
                visibleValues, secretFields);
    }
}
