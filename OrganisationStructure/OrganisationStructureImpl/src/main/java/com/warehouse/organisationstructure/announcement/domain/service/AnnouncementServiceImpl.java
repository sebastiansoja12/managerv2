package com.warehouse.organisationstructure.announcement.domain.service;

import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImpl(final AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public Optional<Announcement> findActive() {
        return announcementRepository.findActive();
    }

    @Override
    @Transactional
    public Announcement publish(final String message) {
        final Instant now = Instant.now();
        final Announcement announcement = announcementRepository.findActive()
                .map(current -> current.updateMessage(message, now))
                .orElseGet(() -> Announcement.create(message, now));
        return announcementRepository.save(announcement);
    }

    @Override
    @Transactional
    public void clear() {
        announcementRepository.clear();
    }
}
