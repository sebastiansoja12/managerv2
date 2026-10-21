package com.warehouse.onboarding.application.port.primary.result;

import com.warehouse.onboarding.domain.enumeration.TutorialStatus;
import com.warehouse.onboarding.domain.vo.ConsentVersion;

import java.util.List;

public record UserOnboardingResult(
        boolean consentsRequired,
        ConsentVersion consentVersion,
        List<ConsentRequirementResult> consents,
        TutorialStatus tutorialStatus) {
}
