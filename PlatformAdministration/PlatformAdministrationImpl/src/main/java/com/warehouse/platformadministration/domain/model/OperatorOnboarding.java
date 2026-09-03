package com.warehouse.platformadministration.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.vo.OnboardingStep;

import java.util.List;

public record OperatorOnboarding(OperatorId operatorId, List<OnboardingStep> steps) {

    public OperatorOnboarding {
        steps = List.copyOf(steps);
    }

    public long completedSteps() {
        return steps.stream().filter(OnboardingStep::completed).count();
    }

    public boolean complete() {
        return completedSteps() == steps.size();
    }
}
