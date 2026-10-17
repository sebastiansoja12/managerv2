package com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementRepository;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.entity.AnnouncementEntity;

import java.util.List;
import java.util.Optional;

public class AnnouncementRepositoryImpl implements AnnouncementRepository {

    private final AnnouncementReadRepository repository;

    public AnnouncementRepositoryImpl(final AnnouncementReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Announcement> findAll() {
        return repository.findAll()
                .stream()
                .map(AnnouncementEntity::toModel)
                .toList();
    }

    @Override
    public Optional<Announcement> findById(final AnnouncementId announcementId) {
        return repository.findById(announcementId.getValue()).map(AnnouncementEntity::toModel);
    }

    @Override
    public Announcement save(final Announcement announcement) {
        return repository.save(AnnouncementEntity.from(announcement)).toModel();
    }

    @Override
    public void delete(final AnnouncementId announcementId) {
        repository.deleteById(announcementId.getValue());
    }
}
