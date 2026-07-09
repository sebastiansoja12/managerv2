package com.warehouse.dangerousgood.domain.validator;

import org.springframework.stereotype.Component;

@Component
public class NameRequirementValidator {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 60;
    private static final String ALLOWED_PATTERN = "^[A-Z][A-Z0-9 .,/\\-]{2,59}$";

    public static void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dangerous good name cannot be empty.");
        }

        final String value = name.trim();

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Name length must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters.");
        }

        if (!value.matches(ALLOWED_PATTERN)) {
            throw new IllegalArgumentException("Invalid name format. Use uppercase letters, digits, spaces or . , - / and start with a letter.");
        }

        if (value.contains("  ") || value.matches(".*[.,/\\-]{2,}.*")) {
            throw new IllegalArgumentException("Name cannot contain consecutive spaces or special characters.");
        }

        if (!value.matches(".*[A-Z].*") || !value.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Name must contain at least one letter and one digit.");
        }
    }
}

