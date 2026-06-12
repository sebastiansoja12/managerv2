package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.helper.Result;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementService;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper.AnnouncementRequestMapper;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper.AnnouncementResponseMapper;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.validator.AnnouncementRequestValidator;
import com.warehouse.organisationstructure.api.dto.AnnouncementDto;
import com.warehouse.organisationstructure.api.dto.AnnouncementRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/announcements")
@AccessUserControl
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final AnnouncementRequestValidator announcementRequestValidator;

    public AnnouncementController(final AnnouncementService announcementService,
                                  final AnnouncementRequestValidator announcementRequestValidator) {
        this.announcementService = announcementService;
        this.announcementRequestValidator = announcementRequestValidator;
    }

    @GetMapping("/active")
    public ResponseEntity<AnnouncementDto> getActive() {
        return announcementService.findActive()
                .map(announcement -> ResponseEntity.ok(AnnouncementResponseMapper.toDto(announcement)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/active")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE})
    public ResponseEntity<?> publish(@RequestBody final AnnouncementRequestDto request) {
        final Result<Void, List<String>> validationResult = announcementRequestValidator.validate(request);
        if (validationResult.isFailure()) {
            return ResponseEntity.badRequest().body(validationResult.getFailure());
        }
        final String message = AnnouncementRequestMapper.toMessage(request);
        return ResponseEntity.ok(AnnouncementResponseMapper.toDto(announcementService.publish(message)));
    }

    @DeleteMapping("/active")
    public ResponseEntity<Void> clear() {
        announcementService.clear();
        return ResponseEntity.noContent().build();
    }
}
