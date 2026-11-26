package com.warehouse.logisticsdelivery.domain.vo;

import java.time.Instant;

public record AssignmentWindow(Instant from, Instant to, Instant backlogFrom) {

    public AssignmentWindow {
        if (!from.isBefore(to)) {
            throw new IllegalArgumentException("Assignment window must start before it ends");
        }
        if (backlogFrom.isAfter(from)) {
            throw new IllegalArgumentException("Backlog must start before the assignment window");
        }
    }
}
