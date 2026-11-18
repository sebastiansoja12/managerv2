package com.warehouse.onboarding.application.port.primary.result;

import com.warehouse.onboarding.domain.enumeration.ConsentType;

public record ConsentRequirementResult(ConsentType type, boolean required, boolean accepted) {
}
