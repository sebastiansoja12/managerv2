package com.warehouse.tracking.domain.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.tracking.domain.enumeration.TrackingProviderId;

public class TrackingIntegrationConfiguration extends BelongsToOperator {

    private final UUID configurationId;
    private final TrackingProviderId provider;
    private boolean enabled;
    private Map<String, String> values;

    public TrackingIntegrationConfiguration(final UUID configurationId,
                                            final TrackingProviderId provider,
                                            final boolean enabled,
                                            final Map<String, String> values) {
        this.configurationId = configurationId;
        this.provider = provider;
        this.enabled = enabled;
        this.values = new LinkedHashMap<>(values);
    }

    public void update(final boolean enabled, final Map<String, String> values) {
        this.enabled = enabled;
        this.values = new LinkedHashMap<>(values);
    }

    public boolean isConfigured(final TrackingIntegrationDefinition definition) {
        return definition.fields().stream()
                .filter(TrackingIntegrationFieldDefinition::required)
                .allMatch(field -> hasValue(field.key()));
    }

    public UUID getConfigurationId() {
        return configurationId;
    }

    public TrackingProviderId getProvider() {
        return provider;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getValue(final String key) {
        return values.get(key);
    }

    public boolean hasValue(final String key) {
        final String value = values.get(key);
        return value != null && !value.isBlank();
    }

    public Map<String, String> getValues() {
        return Map.copyOf(values);
    }
}
