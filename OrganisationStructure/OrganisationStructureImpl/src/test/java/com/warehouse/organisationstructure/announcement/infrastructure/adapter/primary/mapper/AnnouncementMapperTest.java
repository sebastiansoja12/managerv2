package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.api.dto.AnnouncementDto;
import com.warehouse.organisationstructure.api.dto.AnnouncementRequestDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnouncementMapperTest {

    @Test
    void shouldMapRequestToTrimmedMessage() {
        final String message = AnnouncementRequestMapper.toMessage(
                new AnnouncementRequestDto("  Planned maintenance  ")
        );

        assertEquals("Planned maintenance", message);
    }

    @Test
    void shouldMapAnnouncementToDto() {
        final AnnouncementId announcementId = AnnouncementId.of(1L);
        final Instant createdAt = Instant.parse("2026-08-13T10:00:00Z");
        final Instant updatedAt = Instant.parse("2026-08-13T11:00:00Z");
        final Announcement announcement = Announcement.restore(
                announcementId,
                "Planned maintenance",
                createdAt,
                updatedAt
        );

        final AnnouncementDto response = AnnouncementResponseMapper.toDto(announcement);

        assertEquals(announcementId, response.id());
        assertEquals("Planned maintenance", response.message());
        assertEquals(createdAt, response.createdAt());
        assertEquals(updatedAt, response.updatedAt());
    }
}
