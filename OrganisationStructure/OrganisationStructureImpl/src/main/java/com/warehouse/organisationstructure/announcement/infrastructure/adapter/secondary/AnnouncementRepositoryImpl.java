package com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementRepository;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.entity.AnnouncementEntity;

import java.util.Optional;

public class AnnouncementRepositoryImpl implements AnnouncementRepository {

    private final AnnouncementReadRepository repository;

    public AnnouncementRepositoryImpl(final AnnouncementReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Announcement> findActive() {
        return repository.findFirstByOrderByUpdatedAtDesc().map(AnnouncementEntity::toModel);
    }

    @Override
    public Announcement save(final Announcement announcement) {
        return repository.save(AnnouncementEntity.from(announcement)).toModel();
    }

    @Override
    public void clear() {
        repository.deleteAllInBatch();
    }
}
