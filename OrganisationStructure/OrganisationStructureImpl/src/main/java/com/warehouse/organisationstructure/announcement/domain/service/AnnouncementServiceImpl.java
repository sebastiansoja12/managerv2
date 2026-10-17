package com.warehouse.organisationstructure.announcement.domain.service;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.exception.AnnouncementNotFoundException;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementStatus;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementAudienceServicePort;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementRepository;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementAudience;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementDraft;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AnnouncementServiceImpl implements AnnouncementService {

    private static final Comparator<Announcement> VISIBLE_ORDER = Comparator
            .comparing((Announcement announcement) -> announcement.getSeverity().priority())
            .reversed()
            .thenComparing(Announcement::getUpdatedAt, Comparator.reverseOrder());

    private final AnnouncementRepository announcementRepository;

    private final AnnouncementAudienceServicePort announcementAudienceServicePort;

    private final Clock clock;

    public AnnouncementServiceImpl(final AnnouncementRepository announcementRepository,
                                   final AnnouncementAudienceServicePort announcementAudienceServicePort,
                                   final Clock clock) {
        this.announcementRepository = announcementRepository;
        this.announcementAudienceServicePort = announcementAudienceServicePort;
        this.clock = clock;
    }

    @Override
    public List<Announcement> findAll() {
        return announcementRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Announcement::getUpdatedAt, Comparator.reverseOrder()))
                .toList();
    }

    @Override
    public List<Announcement> findVisible() {
        final Instant now = Instant.now(clock);
        final AnnouncementAudience audience = announcementAudienceServicePort.currentAudience().orElse(null);
        return announcementRepository.findAll()
                .stream()
                .filter(announcement -> announcement.isVisibleTo(audience, now))
                .sorted(VISIBLE_ORDER)
                .toList();
    }

    @Override
    public Optional<Announcement> findActive() {
        return findVisible().stream().findFirst();
    }

    @Override
    @Transactional
    public Announcement create(final AnnouncementDraft draft) {
        return announcementRepository.save(Announcement.create(draft,
                announcementAudienceServicePort.currentAuthor(), Instant.now(clock)));
    }

    @Override
    @Transactional
    public Announcement update(final AnnouncementId announcementId, final AnnouncementDraft draft) {
        final Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
        return announcementRepository.save(announcement.update(draft, Instant.now(clock)));
    }

    @Override
    @Transactional
    public void delete(final AnnouncementId announcementId) {
        if (announcementRepository.findById(announcementId).isEmpty()) {
            throw new AnnouncementNotFoundException(announcementId);
        }
        announcementRepository.delete(announcementId);
    }

    @Override
    public AnnouncementStatus statusOf(final Announcement announcement) {
        return announcement.statusAt(Instant.now(clock));
    }
}
