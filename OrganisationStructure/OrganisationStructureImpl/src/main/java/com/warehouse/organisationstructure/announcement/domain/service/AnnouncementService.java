package com.warehouse.organisationstructure.announcement.domain.service;

import com.warehouse.organisationstructure.announcement.domain.model.Announcement;

import java.util.Optional;

public interface AnnouncementService {

    Optional<Announcement> findActive();

    Announcement publish(String message);

    void clear();
}
