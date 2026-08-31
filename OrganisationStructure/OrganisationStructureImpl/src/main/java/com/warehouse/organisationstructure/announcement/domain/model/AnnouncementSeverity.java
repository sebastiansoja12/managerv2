package com.warehouse.organisationstructure.announcement.domain.model;

public enum AnnouncementSeverity {
    INFO(0),
    WARNING(1),
    CRITICAL(2);

    private final int priority;

    AnnouncementSeverity(final int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }
}
