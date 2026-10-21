package com.warehouse.onboarding.domain.vo;

import java.util.Objects;

public record ConsentVersion(String value) {

    public ConsentVersion {
        Objects.requireNonNull(value, "Consent version cannot be null");
    }
}
