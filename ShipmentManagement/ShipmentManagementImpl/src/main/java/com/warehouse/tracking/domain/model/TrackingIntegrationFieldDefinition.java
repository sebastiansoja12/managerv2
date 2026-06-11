package com.warehouse.tracking.domain.model;

import java.util.List;
import java.util.Optional;

import com.warehouse.tracking.domain.exception.TrackingErrorCode;
import com.warehouse.tracking.domain.exception.TrackingException;

public record TrackingIntegrationFieldDefinition(String key,
                                                 String label,
                                                 TrackingIntegrationFieldType type,
                                                 boolean required,
                                                 String defaultValue,
                                                 int maxLength,
                                                 List<TrackingIntegrationFieldOption> options) {

    public boolean secret() {
        return type == TrackingIntegrationFieldType.SECRET;
    }

    public Optional<String> resolveValue(final String submittedValue, final String storedValue) {
        String value = submittedValue;
        if (secret() && isBlank(value)) {
            value = storedValue;
        }
        if (value != null && !secret()) {
            value = value.trim();
        }

        validate(value);
        return isBlank(value) ? Optional.empty() : Optional.of(value);
    }

    private void validate(final String value) {
        if (required && isBlank(value)) {
            throw configurationError(label + " is required");
        }
        if (value != null && value.length() > maxLength) {
            throw configurationError(label + " exceeds the maximum length");
        }
        if (type == TrackingIntegrationFieldType.SELECT && value != null
                && options.stream().noneMatch(option -> option.value().equals(value))) {
            throw configurationError(label + " contains an unsupported value");
        }
        if (type == TrackingIntegrationFieldType.BOOLEAN && value != null
                && !value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw configurationError(label + " must be true or false");
        }
    }

    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }

    private TrackingException configurationError(final String message) {
        return new TrackingException(TrackingErrorCode.CONFIGURATION_ERROR, 400, message, null);
    }
}
