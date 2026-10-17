package com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("organisationstructure.announcementReadRepository")
public interface AnnouncementReadRepository extends JpaRepository<AnnouncementEntity, Long> {
}
