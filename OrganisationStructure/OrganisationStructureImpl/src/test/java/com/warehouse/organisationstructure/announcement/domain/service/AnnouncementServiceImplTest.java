package com.warehouse.organisationstructure.announcement.domain.service;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.exception.AnnouncementNotFoundException;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementSeverity;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementAudienceServicePort;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementRepository;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementAudience;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementDraft;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnnouncementServiceImplTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private final InMemoryAnnouncementRepository repository = new InMemoryAnnouncementRepository();

    private final FakeAudience audience = new FakeAudience();

    private final AnnouncementService service = new AnnouncementServiceImpl(repository, audience,
            Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldStoreAuthorOfNewAnnouncement() {
        final Announcement announcement = service.create(draft("Aktualizacja", AnnouncementSeverity.INFO, Set.of()));

        assertThat(announcement.getAnnouncementId()).isNotNull();
        assertThat(announcement.getCreatedBy()).isEqualTo("owner");
    }

    @Test
    void shouldReturnOnlyAnnouncementsVisibleToCurrentUserOrderedBySeverity() {
        service.create(draft("Informacja", AnnouncementSeverity.INFO, Set.of()));
        service.create(draft("Awaria", AnnouncementSeverity.CRITICAL, Set.of()));
        service.create(draft("Inny operator", AnnouncementSeverity.CRITICAL, Set.of(99L)));
        audience.audience = Optional.of(new AnnouncementAudience(20L, "USER"));

        final List<Announcement> visible = service.findVisible();

        assertThat(visible).extracting(Announcement::getMessage).containsExactly("Awaria", "Informacja");
        assertThat(service.findActive()).map(Announcement::getMessage).contains("Awaria");
    }

    @Test
    void shouldUpdateExistingAnnouncement() {
        final Announcement announcement = service.create(draft("Stara treść", AnnouncementSeverity.INFO, Set.of()));

        final Announcement updated = service.update(announcement.getAnnouncementId(),
                draft("Nowa treść", AnnouncementSeverity.WARNING, Set.of(20L)));

        assertThat(updated.getAnnouncementId()).isEqualTo(announcement.getAnnouncementId());
        assertThat(updated.getTargetOperatorIds()).containsExactly(20L);
    }

    @Test
    void shouldFailWhenUpdatingOrDeletingUnknownAnnouncement() {
        assertThrows(AnnouncementNotFoundException.class,
                () -> service.update(AnnouncementId.of(5L), draft("x", AnnouncementSeverity.INFO, Set.of())));
        assertThrows(AnnouncementNotFoundException.class, () -> service.delete(AnnouncementId.of(5L)));
    }

    @Test
    void shouldDeleteAnnouncement() {
        final Announcement announcement = service.create(draft("Do usunięcia", AnnouncementSeverity.INFO, Set.of()));

        service.delete(announcement.getAnnouncementId());

        assertThat(service.findAll()).isEmpty();
    }

    private AnnouncementDraft draft(final String message, final AnnouncementSeverity severity, final Set<Long> operators) {
        return new AnnouncementDraft(null, message, severity, null, null, operators, Set.of());
    }

    private static final class FakeAudience implements AnnouncementAudienceServicePort {

        private Optional<AnnouncementAudience> audience = Optional.empty();

        @Override
        public Optional<AnnouncementAudience> currentAudience() {
            return audience;
        }

        @Override
        public String currentAuthor() {
            return "owner";
        }
    }

    private static final class InMemoryAnnouncementRepository implements AnnouncementRepository {

        private final Map<Long, Announcement> announcements = new LinkedHashMap<>();

        private long sequence;

        @Override
        public List<Announcement> findAll() {
            return new ArrayList<>(announcements.values());
        }

        @Override
        public Optional<Announcement> findById(final AnnouncementId announcementId) {
            return Optional.ofNullable(announcements.get(announcementId.getValue()));
        }

        @Override
        public Announcement save(final Announcement announcement) {
            final Long id = announcement.getAnnouncementId() != null ? announcement.getAnnouncementId().getValue() : ++sequence;
            final Announcement stored = Announcement.restore(AnnouncementId.of(id), announcement.getTitle(),
                    announcement.getMessage(), announcement.getSeverity(), announcement.getValidFrom(),
                    announcement.getValidTo(), announcement.getTargetOperatorIds(), announcement.getTargetRoles(),
                    announcement.getCreatedBy(), announcement.getCreatedAt(), announcement.getUpdatedAt());
            announcements.put(id, stored);
            return stored;
        }

        @Override
        public void delete(final AnnouncementId announcementId) {
            announcements.remove(announcementId.getValue());
        }
    }
}
