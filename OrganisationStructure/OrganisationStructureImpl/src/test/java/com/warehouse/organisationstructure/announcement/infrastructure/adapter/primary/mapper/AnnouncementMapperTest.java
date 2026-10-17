package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementSeverity;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementStatus;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementDraft;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api.AnnouncementRequest;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api.PlatformAnnouncementResponse;
import com.warehouse.organisationstructure.api.dto.AnnouncementDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnnouncementMapperTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    @Test
    void shouldKeepLegacyActiveAnnouncementContract() {
        final AnnouncementDto response = AnnouncementResponseMapper.toDto(announcement());

        assertThat(response).isEqualTo(new AnnouncementDto(AnnouncementId.of(1L), "Planowany przestój", NOW, NOW));
    }

    @Test
    void shouldMapTargetsAndStatusForPanel() {
        final PlatformAnnouncementResponse response = AnnouncementResponseMapper.toPlatformResponse(announcement(),
                AnnouncementStatus.ACTIVE);

        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(response.targetOperatorIds()).containsExactly(20L);
        assertThat(response.targetRoles()).containsExactly("ADMIN");
        assertThat(response.createdBy()).isEqualTo("owner");
    }

    @Test
    void shouldDefaultSeverityToInfoAndParseCaseInsensitive() {
        final AnnouncementDraft defaultDraft = AnnouncementResponseMapper.toDraft(new AnnouncementRequest(null,
                "Treść", null, null, null, null, null));
        final AnnouncementDraft criticalDraft = AnnouncementResponseMapper.toDraft(new AnnouncementRequest(null,
                "Treść", " critical ", null, null, List.of(20L), List.of("ADMIN")));

        assertThat(defaultDraft.severity()).isEqualTo(AnnouncementSeverity.INFO);
        assertThat(defaultDraft.targetOperatorIds()).isNull();
        assertThat(criticalDraft.severity()).isEqualTo(AnnouncementSeverity.CRITICAL);
        assertThat(criticalDraft.targetOperatorIds()).containsExactly(20L);
    }

    @Test
    void shouldRejectUnknownSeverity() {
        assertThrows(IllegalArgumentException.class, () -> AnnouncementResponseMapper.toDraft(new AnnouncementRequest(
                null, "Treść", "PANIC", null, null, null, null)));
    }

    private Announcement announcement() {
        return Announcement.restore(AnnouncementId.of(1L), "Przestój", "Planowany przestój",
                AnnouncementSeverity.WARNING, null, null, Set.of(20L), Set.of("ADMIN"), "owner", NOW, NOW);
    }
}
