package com.warehouse.platformadministration.domain.vo;

public record OnboardingStep(OnboardingStepType type, boolean completed, long count) {

    public static OnboardingStep counted(final OnboardingStepType type, final long count) {
        return new OnboardingStep(type, count > 0, count);
    }

    public static OnboardingStep flag(final OnboardingStepType type, final boolean completed) {
        return new OnboardingStep(type, completed, completed ? 1 : 0);
    }
}
