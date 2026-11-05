package com.warehouse.onboarding.infrastructure.adapter.primary.dto;

public record ConsentRequirementApiResponse(String type, boolean required, boolean accepted) {
}
