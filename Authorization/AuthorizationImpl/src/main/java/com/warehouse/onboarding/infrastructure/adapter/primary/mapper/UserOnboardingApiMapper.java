package com.warehouse.onboarding.infrastructure.adapter.primary.mapper;

import com.warehouse.onboarding.application.port.primary.command.AcceptConsentsCommand;
import com.warehouse.onboarding.application.port.primary.result.UserOnboardingResult;
import com.warehouse.onboarding.domain.enumeration.ConsentType;
import com.warehouse.onboarding.domain.vo.ConsentVersion;
import com.warehouse.onboarding.infrastructure.adapter.primary.dto.AcceptConsentsApiRequest;
import com.warehouse.onboarding.infrastructure.adapter.primary.dto.ConsentRequirementApiResponse;
import com.warehouse.onboarding.infrastructure.adapter.primary.dto.UserOnboardingApiResponse;

import java.util.stream.Collectors;

public abstract class UserOnboardingApiMapper {

    public static AcceptConsentsCommand toCommand(final AcceptConsentsApiRequest request) {
        return new AcceptConsentsCommand(
                request.acceptedConsents().stream()
                        .map(ConsentType::valueOf)
                        .collect(Collectors.toUnmodifiableSet()),
                new ConsentVersion(request.version()));
    }

    public static UserOnboardingApiResponse toResponse(final UserOnboardingResult result) {
        return new UserOnboardingApiResponse(
                result.consentsRequired(),
                result.consentVersion().value(),
                result.consents().stream()
                        .map(consent -> new ConsentRequirementApiResponse(consent.type().name(), consent.required(),
                                consent.accepted()))
                        .toList(),
                result.tutorialStatus().name());
    }
}
