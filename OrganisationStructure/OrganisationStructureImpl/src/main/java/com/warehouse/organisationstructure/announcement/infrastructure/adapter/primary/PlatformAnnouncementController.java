package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.exception.AnnouncementNotFoundException;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementService;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api.AnnouncementRequest;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api.PlatformAnnouncementResponse;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper.AnnouncementResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/platform/announcements")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class PlatformAnnouncementController {

    private final AnnouncementService announcementService;

    public PlatformAnnouncementController(final AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ResponseEntity<List<PlatformAnnouncementResponse>> findAll() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(announcementService.findAll()
                        .stream()
                        .map(this::toResponse)
                        .toList());
    }

    @PostMapping
    public ResponseEntity<PlatformAnnouncementResponse> create(@Valid @RequestBody final AnnouncementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(announcementService.create(AnnouncementResponseMapper.toDraft(request))));
    }

    @PutMapping("/{announcementId}")
    public ResponseEntity<PlatformAnnouncementResponse> update(@PathVariable final Long announcementId,
                                                               @Valid @RequestBody final AnnouncementRequest request) {
        return ResponseEntity.ok(toResponse(announcementService.update(AnnouncementId.of(announcementId),
                AnnouncementResponseMapper.toDraft(request))));
    }

    @DeleteMapping("/{announcementId}")
    public ResponseEntity<Void> delete(@PathVariable final Long announcementId) {
        announcementService.delete(AnnouncementId.of(announcementId));
        return ResponseEntity.noContent().build();
    }

    private PlatformAnnouncementResponse toResponse(final Announcement announcement) {
        return AnnouncementResponseMapper.toPlatformResponse(announcement, announcementService.statusOf(announcement));
    }

    @ExceptionHandler(AnnouncementNotFoundException.class)
    public ResponseEntity<String> handleNotFound(final AnnouncementNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
