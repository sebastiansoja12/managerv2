package com.warehouse.organisationstructure.announcement.domain.service;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnouncementServiceImplTest {

    private final InMemoryAnnouncementRepository repository = new InMemoryAnnouncementRepository();
    private final AnnouncementService service = new AnnouncementServiceImpl(repository);

    @Test
    void publishesAndUpdatesOneGlobalAnnouncement() {
        final Announcement first = service.publish("Planned maintenance");
        final Instant createdAt = first.getCreatedAt();

        final Announcement updated = service.publish("Maintenance completed");

        assertNotNull(first.getAnnouncementId());
        assertEquals(first.getAnnouncementId(), updated.getAnnouncementId());
        assertEquals(createdAt, updated.getCreatedAt());
        assertEquals("Maintenance completed", service.findActive().orElseThrow().getMessage());
    }

    @Test
    void clearsTheActiveAnnouncement() {
        service.publish("Planned maintenance");

        service.clear();

        assertTrue(service.findActive().isEmpty());
    }

    @Test
    void rejectsBlankMessages() {
        assertThrows(IllegalArgumentException.class, () -> service.publish("   "));
    }

    private static final class InMemoryAnnouncementRepository implements AnnouncementRepository {
        private Announcement announcement;

        @Override
        public Optional<Announcement> findActive() {
            return Optional.ofNullable(announcement);
        }

        @Override
        public Announcement save(final Announcement value) {
            announcement = value.getAnnouncementId() == null
                    ? Announcement.restore(AnnouncementId.of(1L), value.getMessage(), value.getCreatedAt(), value.getUpdatedAt())
                    : value;
            return announcement;
        }

        @Override
        public void clear() {
            announcement = null;
        }
    }
}
