package com.warehouse.platformadministration.domain.model;

import java.time.Instant;
import java.util.List;

public record MaintenanceMode(boolean enabled, String message, String updatedBy, Instant updatedAt) {

    public static final int MAX_MESSAGE_LENGTH = 500;

    private static final List<String> ALWAYS_AVAILABLE_PATHS = List.of(
            "/platform",
            "/operators",
            "/maintenance",
            "/auth/csrf",
            "/auth/logout"
    );

    private static final String ANNOUNCEMENT_PATH = "/announcements/";

    public static MaintenanceMode disabled() {
        return new MaintenanceMode(false, null, null, null);
    }

    public static MaintenanceMode change(final boolean enabled,
                                         final String message,
                                         final String updatedBy,
                                         final Instant updatedAt) {
        final String normalizedMessage = message == null || message.isBlank() ? null : message.trim();
        if (normalizedMessage != null && normalizedMessage.length() > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("Maintenance message cannot be longer than " + MAX_MESSAGE_LENGTH);
        }
        return new MaintenanceMode(enabled, normalizedMessage, updatedBy, updatedAt);
    }

    public boolean blocks(final String path, final String httpMethod, final boolean platformActor) {
        if (!enabled || platformActor || path == null) {
            return false;
        }
        if (path.startsWith(ANNOUNCEMENT_PATH) && "GET".equalsIgnoreCase(httpMethod)) {
            return false;
        }
        return ALWAYS_AVAILABLE_PATHS.stream()
                .noneMatch(available -> path.equals(available) || path.startsWith(available + "/"));
    }
}
