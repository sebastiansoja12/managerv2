package com.warehouse.onboarding.infrastructure.adapter.primary.dto;

import java.util.List;

public record UserOnboardingApiResponse(
        boolean consentsRequired,
        String consentVersion,
        List<ConsentRequirementApiResponse> consents,
        String tutorialStatus) {
}
