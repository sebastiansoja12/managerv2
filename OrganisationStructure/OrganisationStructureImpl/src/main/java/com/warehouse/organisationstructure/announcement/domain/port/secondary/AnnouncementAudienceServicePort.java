package com.warehouse.organisationstructure.announcement.domain.port.secondary;

import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementAudience;

import java.util.Optional;

public interface AnnouncementAudienceServicePort {

    Optional<AnnouncementAudience> currentAudience();

    String currentAuthor();
}
