package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementService;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.validator.AnnouncementRequestValidator;
import com.warehouse.organisationstructure.api.dto.AnnouncementDto;
import com.warehouse.organisationstructure.api.dto.AnnouncementRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnnouncementControllerTest {

    @Mock
    private AnnouncementService announcementService;

    @Test
    void shouldRequireAuthenticationForControllerAndAdminCreatePermissionForPublish() throws NoSuchMethodException {
        assertNotNull(AnnouncementController.class.getAnnotation(AccessUserControl.class));
        final Method publishMethod = AnnouncementController.class.getMethod(
                "publish",
                AnnouncementRequestDto.class
        );
        final AccessUserControl accessUserControl = publishMethod.getAnnotation(AccessUserControl.class);

        assertNotNull(accessUserControl);
        assertArrayEquals(
                new UserPermission[]{UserPermission.ROLE_ADMIN_CREATE},
                accessUserControl.permissions()
        );
    }

    @Test
    void shouldRejectInvalidPublishRequestBeforeCallingService() {
        final AnnouncementController controller = controller();

        final ResponseEntity<?> response = controller.publish(new AnnouncementRequestDto("   "));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(List.of("Announcement message must not be blank"), response.getBody());
        verifyNoInteractions(announcementService);
    }

    @Test
    void shouldPublishMappedAnnouncement() {
        final Instant now = Instant.parse("2026-08-13T10:00:00Z");
        final Announcement announcement = Announcement.restore(
                AnnouncementId.of(1L),
                "Planned maintenance",
                now,
                now
        );
        when(announcementService.publish("Planned maintenance")).thenReturn(announcement);
        final AnnouncementController controller = controller();

        final ResponseEntity<?> response = controller.publish(
                new AnnouncementRequestDto("  Planned maintenance  ")
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                new AnnouncementDto(AnnouncementId.of(1L), "Planned maintenance", now, now),
                response.getBody()
        );
        verify(announcementService).publish("Planned maintenance");
    }

    private AnnouncementController controller() {
        return new AnnouncementController(announcementService, new AnnouncementRequestValidator());
    }
}
