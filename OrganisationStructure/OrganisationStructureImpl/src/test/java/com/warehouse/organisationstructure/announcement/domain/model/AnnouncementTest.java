package com.warehouse.organisationstructure.announcement.domain.model;

import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementAudience;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementDraft;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnnouncementTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    @Test
    void shouldResolveStatusFromValidityWindow() {
        final Announcement announcement = announcement(NOW.plusSeconds(3600), NOW.plusSeconds(7200), Set.of(), Set.of());

        assertThat(announcement.statusAt(NOW)).isEqualTo(AnnouncementStatus.SCHEDULED);
        assertThat(announcement.statusAt(NOW.plusSeconds(3600))).isEqualTo(AnnouncementStatus.ACTIVE);
        assertThat(announcement.statusAt(NOW.plusSeconds(7200))).isEqualTo(AnnouncementStatus.EXPIRED);
    }

    @Test
    void shouldShowGlobalAnnouncementToEveryone() {
        final Announcement announcement = announcement(null, null, Set.of(), Set.of());

        assertThat(announcement.isGlobal()).isTrue();
        assertThat(announcement.isVisibleTo(new AnnouncementAudience(20L, "USER"), NOW)).isTrue();
    }

    @Test
    void shouldShowTargetedAnnouncementOnlyToMatchingOperatorAndRole() {
        final Announcement announcement = announcement(null, null, Set.of(20L), Set.of("ADMIN", "SUPERADMIN"));

        assertThat(announcement.isVisibleTo(new AnnouncementAudience(20L, "ADMIN"), NOW)).isTrue();
        assertThat(announcement.isVisibleTo(new AnnouncementAudience(20L, "USER"), NOW)).isFalse();
        assertThat(announcement.isVisibleTo(new AnnouncementAudience(21L, "ADMIN"), NOW)).isFalse();
    }

    @Test
    void shouldShowEveryActiveAnnouncementToPlatformOwner() {
        final Announcement announcement = announcement(null, null, Set.of(20L), Set.of("ADMIN"));

        assertThat(announcement.isVisibleTo(null, NOW)).isTrue();
    }

    @Test
    void shouldNotShowExpiredAnnouncement() {
        final Announcement announcement = announcement(null, NOW.minusSeconds(1), Set.of(), Set.of());

        assertThat(announcement.isVisibleTo(new AnnouncementAudience(20L, "USER"), NOW)).isFalse();
    }

    @Test
    void shouldRejectInvertedValidityWindow() {
        assertThrows(IllegalArgumentException.class,
                () -> announcement(NOW.plusSeconds(10), NOW, Set.of(), Set.of()));
    }

    @Test
    void shouldRejectUnknownRole() {
        assertThrows(IllegalArgumentException.class,
                () -> announcement(null, null, Set.of(), Set.of("PLATFORM_OWNER")));
    }

    @Test
    void shouldKeepAuthorAndCreationDateOnUpdate() {
        final Announcement announcement = announcement(null, null, Set.of(), Set.of());

        final Announcement updated = announcement.update(new AnnouncementDraft(null, "Nowa treść",
                AnnouncementSeverity.CRITICAL, null, null, Set.of(), Set.of()), NOW.plusSeconds(60));

        assertThat(updated.getCreatedBy()).isEqualTo("owner");
        assertThat(updated.getCreatedAt()).isEqualTo(NOW);
        assertThat(updated.getUpdatedAt()).isEqualTo(NOW.plusSeconds(60));
        assertThat(updated.getSeverity()).isEqualTo(AnnouncementSeverity.CRITICAL);
    }

    private Announcement announcement(final Instant validFrom,
                                      final Instant validTo,
                                      final Set<Long> operators,
                                      final Set<String> roles) {
        return Announcement.create(new AnnouncementDraft("Przestój", "Planowany przestój systemu",
                AnnouncementSeverity.WARNING, validFrom, validTo, operators, roles), "owner", NOW);
    }
}
