package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementService;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api.VisibleAnnouncementResponse;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper.AnnouncementResponseMapper;
import com.warehouse.organisationstructure.api.dto.AnnouncementDto;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/announcements")
@AccessUserControl(audience = AccessAudience.ANY_ACCOUNT)
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(final AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/active")
    public ResponseEntity<AnnouncementDto> getActive() {
        return announcementService.findActive()
                .map(announcement -> ResponseEntity.ok(AnnouncementResponseMapper.toDto(announcement)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/current")
    public ResponseEntity<List<VisibleAnnouncementResponse>> getCurrent() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(announcementService.findVisible()
                        .stream()
                        .map(AnnouncementResponseMapper::toVisibleResponse)
                        .toList());
    }
}
