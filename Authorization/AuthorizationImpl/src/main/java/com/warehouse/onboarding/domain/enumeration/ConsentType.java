package com.warehouse.onboarding.domain.enumeration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ConsentType {
    TERMS_OF_SERVICE(true),
    PRIVACY_POLICY(true),
    PERSONAL_DATA_PROCESSING(true),
    PRODUCT_UPDATES(false);

    private final boolean required;

    ConsentType(final boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public static Set<ConsentType> required() {
        return Arrays.stream(values())
                .filter(ConsentType::isRequired)
                .collect(Collectors.toUnmodifiableSet());
    }
}
