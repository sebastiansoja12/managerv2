package com.warehouse.organisationstructure.announcement.domain.service;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementStatus;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementDraft;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {

    List<Announcement> findAll();

    List<Announcement> findVisible();

    Optional<Announcement> findActive();

    Announcement create(final AnnouncementDraft draft);

    Announcement update(final AnnouncementId announcementId, final AnnouncementDraft draft);

    void delete(final AnnouncementId announcementId);

    AnnouncementStatus statusOf(final Announcement announcement);
}
