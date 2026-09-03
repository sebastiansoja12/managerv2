package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.util.List;

public record OperatorOnboardingResponse(Long operatorId, long completedSteps, int totalSteps, boolean complete,
                                         List<Step> steps) {

    public record Step(String type, boolean completed, long count) {
    }
}
