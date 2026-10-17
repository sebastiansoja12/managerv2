package com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.PlatformActorService;
import com.warehouse.auth.infrastructure.dto.PlatformActorDto;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementAudienceServicePort;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementAudience;

import java.util.Optional;

public class AnnouncementAudienceServiceAdapter implements AnnouncementAudienceServicePort {

    private final PlatformActorService platformActorService;

    private final CurrentUserApiService currentUserApiService;

    public AnnouncementAudienceServiceAdapter(final PlatformActorService platformActorService,
                                              final CurrentUserApiService currentUserApiService) {
        this.platformActorService = platformActorService;
        this.currentUserApiService = currentUserApiService;
    }

    @Override
    public Optional<AnnouncementAudience> currentAudience() {
        final Optional<PlatformActorDto> actor = platformActorService.currentActor();
        if (actor.isPresent() && !actor.get().impersonation()) {
            return Optional.empty();
        }
        final UserDto user = currentUserApiService.getCurrentUser();
        if (user == null) {
            return Optional.of(new AnnouncementAudience(null, null));
        }
        return Optional.of(new AnnouncementAudience(
                user.operatorId() != null ? user.operatorId().value() : null, user.role()));
    }

    @Override
    public String currentAuthor() {
        return platformActorService.currentActor()
                .map(PlatformActorDto::username)
                .orElse(null);
    }
}
