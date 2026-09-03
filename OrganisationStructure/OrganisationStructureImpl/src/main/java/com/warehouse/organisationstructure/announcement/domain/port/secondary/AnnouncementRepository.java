package com.warehouse.organisationstructure.announcement.domain.port.secondary;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository {

    List<Announcement> findAll();

    Optional<Announcement> findById(final AnnouncementId announcementId);

    Announcement save(final Announcement announcement);

    void delete(final AnnouncementId announcementId);
}
