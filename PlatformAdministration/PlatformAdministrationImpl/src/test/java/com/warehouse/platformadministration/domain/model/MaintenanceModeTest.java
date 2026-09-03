package com.warehouse.platformadministration.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaintenanceModeTest {

    private final MaintenanceMode enabled = MaintenanceMode.change(true, "Prace serwisowe", "owner", Instant.now());

    @Test
    void shouldNotBlockAnythingWhenDisabled() {
        assertThat(MaintenanceMode.disabled().blocks("/shipments", "POST", false)).isFalse();
    }

    @Test
    void shouldBlockOrganizationRequestsDuringMaintenance() {
        assertThat(enabled.blocks("/shipments", "GET", false)).isTrue();
        assertThat(enabled.blocks("/auth/login", "POST", false)).isTrue();
        assertThat(enabled.blocks("/auth/refresh", "POST", false)).isTrue();
    }

    @Test
    void shouldKeepPlatformPanelAndPublicStatusAvailable() {
        assertThat(enabled.blocks("/platform/auth/login", "POST", false)).isFalse();
        assertThat(enabled.blocks("/operators/10/users", "GET", false)).isFalse();
        assertThat(enabled.blocks("/maintenance", "GET", false)).isFalse();
        assertThat(enabled.blocks("/auth/csrf", "GET", false)).isFalse();
        assertThat(enabled.blocks("/announcements/active", "GET", false)).isFalse();
    }

    @Test
    void shouldNotTreatSimilarPathAsPlatformPath() {
        assertThat(enabled.blocks("/platformer", "GET", false)).isTrue();
        assertThat(enabled.blocks("/announcements/active", "PUT", false)).isTrue();
    }

    @Test
    void shouldLetPlatformActorWorkDuringMaintenance() {
        assertThat(enabled.blocks("/shipments", "POST", true)).isFalse();
    }

    @Test
    void shouldNormalizeBlankMessage() {
        assertThat(MaintenanceMode.change(true, "   ", "owner", Instant.now()).message()).isNull();
        assertThat(MaintenanceMode.change(true, "  Przerwa  ", "owner", Instant.now()).message()).isEqualTo("Przerwa");
    }

    @Test
    void shouldRejectTooLongMessage() {
        assertThrows(IllegalArgumentException.class,
                () -> MaintenanceMode.change(true, "x".repeat(501), "owner", Instant.now()));
    }
}
