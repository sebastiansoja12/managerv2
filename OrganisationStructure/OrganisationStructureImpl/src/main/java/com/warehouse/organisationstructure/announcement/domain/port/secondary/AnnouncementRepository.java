package com.warehouse.organisationstructure.announcement.domain.port.secondary;

import com.warehouse.organisationstructure.announcement.domain.model.Announcement;

import java.util.Optional;

public interface AnnouncementRepository {

    Optional<Announcement> findActive();

    Announcement save(Announcement announcement);

    void clear();
}
