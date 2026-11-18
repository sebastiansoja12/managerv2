package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.exception.AnnouncementNotFoundException;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementSeverity;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementStatus;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementService;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementDraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class PlatformAnnouncementControllerTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private final AnnouncementService announcementService = mock(AnnouncementService.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new PlatformAnnouncementController(announcementService))
                .setMessageConverters(new JacksonJsonHttpMessageConverter())
                .build();
    }

    @Test
    void shouldListAnnouncementsWithStatus() throws Exception {
        final Announcement announcement = Announcement.restore(AnnouncementId.of(1L), null, "Przyszłe ogłoszenie",
                AnnouncementSeverity.INFO, NOW.plusSeconds(60), null, Set.of(20L), Set.of("ADMIN"), "owner", NOW, NOW);
        when(announcementService.findAll()).thenReturn(List.of(announcement));
        when(announcementService.statusOf(announcement)).thenReturn(AnnouncementStatus.SCHEDULED);

        mockMvc.perform(get("/platform/announcements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"))
                .andExpect(jsonPath("$[0].targetOperatorIds[0]").value(20))
                .andExpect(jsonPath("$[0].targetRoles[0]").value("ADMIN"));
    }

    @Test
    void shouldCreateTargetedAnnouncement() throws Exception {
        when(announcementService.create(any())).thenAnswer(invocation -> Announcement.create(invocation.getArgument(0),
                "owner", NOW));

        mockMvc.perform(post("/platform/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Przestój","message":"Planowany przestój","severity":"warning",
                                 "validFrom":"2026-10-17T20:00:00Z","validTo":"2026-10-17T22:00:00Z",
                                 "targetOperatorIds":[20],"targetRoles":["ADMIN"]}
                                """))
                .andExpect(status().isCreated());

        final ArgumentCaptor<AnnouncementDraft> draft = ArgumentCaptor.forClass(AnnouncementDraft.class);
        verify(announcementService).create(draft.capture());
        assertThat(draft.getValue().severity()).isEqualTo(AnnouncementSeverity.WARNING);
        assertThat(draft.getValue().targetOperatorIds()).containsExactly(20L);
        assertThat(draft.getValue().validFrom()).isEqualTo(Instant.parse("2026-10-17T20:00:00Z"));
    }

    @Test
    void shouldRejectBlankMessage() throws Exception {
        mockMvc.perform(post("/platform/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"  \"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(announcementService);
    }

    @Test
    void shouldRejectUnknownSeverity() throws Exception {
        mockMvc.perform(post("/platform/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"Test\",\"severity\":\"PANIC\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingUnknownAnnouncement() throws Exception {
        doThrow(new AnnouncementNotFoundException(AnnouncementId.of(9L))).when(announcementService)
                .delete(AnnouncementId.of(9L));

        mockMvc.perform(delete("/platform/announcements/9"))
                .andExpect(status().isNotFound());
    }
}
