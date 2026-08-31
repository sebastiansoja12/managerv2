package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementSeverity;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class AnnouncementControllerTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private final AnnouncementService announcementService = mock(AnnouncementService.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new AnnouncementController(announcementService)).build();
    }

    @Test
    void shouldBeAvailableForEveryAccount() {
        assertThat(AnnouncementController.class.getAnnotation(AccessUserControl.class).audience())
                .isEqualTo(AccessAudience.ANY_ACCOUNT);
    }

    @Test
    void shouldReturnAnnouncementsVisibleToCurrentUser() throws Exception {
        when(announcementService.findVisible()).thenReturn(List.of(announcement()));

        mockMvc.perform(get("/announcements/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].severity").value("CRITICAL"))
                .andExpect(jsonPath("$[0].title").value("Przestój"));
    }

    @Test
    void shouldReturnNoContentWithoutActiveAnnouncement() throws Exception {
        when(announcementService.findActive()).thenReturn(Optional.empty());

        mockMvc.perform(get("/announcements/active"))
                .andExpect(status().isNoContent());
    }

    private Announcement announcement() {
        return Announcement.restore(AnnouncementId.of(3L), "Przestój", "Planowany przestój o 22:00",
                AnnouncementSeverity.CRITICAL, null, null, Set.of(), Set.of(), "owner", NOW, NOW);
    }
}
